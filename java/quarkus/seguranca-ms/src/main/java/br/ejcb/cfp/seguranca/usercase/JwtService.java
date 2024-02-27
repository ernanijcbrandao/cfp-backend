package br.ejcb.cfp.seguranca.usercase;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import br.ejcb.cfp.seguranca.common.enumeration.UserProfile;
import br.ejcb.cfp.seguranca.common.util.PasswordHashingUtils;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JwtService {
	
	private final Random random = new Random();

	@SuppressWarnings("deprecation")
	public String generateToken(String username, 
			UserProfile profile,
			String systemCode,
			String systemName,
			String publickey,
			long duration,
			String issuer) throws Exception {
		
		String privateKeyLocation = "/privatekey.pem";
		PrivateKey privateKey = readPrivateKey(privateKeyLocation);

		JwtClaimsBuilder claimsBuilder = Jwt.claims();
		long currentTimeInSecs = currentTimeInSecs();

		Set<String> groups = new HashSet<>();
		groups.add(profile.toString());
		
		long randomkey = this.random.nextLong();
		randomkey *= (randomkey < 0 ? -1 : 1);

		claimsBuilder.issuer(issuer);
		claimsBuilder.subject(username);
		claimsBuilder.issuedAt(currentTimeInSecs);
		claimsBuilder.expiresAt(currentTimeInSecs + duration);
		claimsBuilder.claim("systemCode", systemCode);
		claimsBuilder.claim("systemName", systemName);
		if (publickey != null && !publickey.isBlank()) {
			claimsBuilder.claim("publickey", publickey); //PasswordHashingUtils.hashPassword(publickey));
		}
		claimsBuilder.claim("seckey", "" + randomkey);
		claimsBuilder.groups(groups);

		return claimsBuilder.jws().signatureKeyId(privateKeyLocation).sign(privateKey);
	}

	public PrivateKey readPrivateKey(final String pemResName) throws Exception {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try (InputStream contentIS = classLoader.getResourceAsStream(pemResName)) {
			byte[] tmp = new byte[4096];
			int length = contentIS.read(tmp);
			return decodePrivateKey(new String(tmp, 0, length, "UTF-8"));
		}
	}

	public PrivateKey decodePrivateKey(final String pemEncoded) throws Exception {
		byte[] encodedBytes = toEncodedBytes(pemEncoded);

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(keySpec);
	}

	public byte[] toEncodedBytes(final String pemEncoded) {
		final String normalizedPem = removeBeginEnd(pemEncoded);
		return Base64.getDecoder().decode(normalizedPem);
	}

	public String removeBeginEnd(String pem) {
		pem = pem.replaceAll("-----BEGIN (.*)-----", "");
		pem = pem.replaceAll("-----END (.*)----", "");
		pem = pem.replaceAll("\r\n", "");
		pem = pem.replaceAll("\n", "");
		return pem.trim();
	}

	public int currentTimeInSecs() {
		long currentTimeMS = System.currentTimeMillis();
		return (int) (currentTimeMS / 1000);
	}
	
}
