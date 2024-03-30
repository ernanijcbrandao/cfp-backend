package br.ejcb.cfp.seguranca.common.enumeration;

public enum UserProfile {

	ROOT,
	ADMIN,
	NORMAL;
	
	public static UserProfile findByName(String name) {
		UserProfile result = null;
		if (name != null) {
			for (UserProfile profile : UserProfile.values()) {
				if (profile.name().equals(name)) {
					result = profile;
					break;
				}
			}
		}
		return result;
	}
	
}
