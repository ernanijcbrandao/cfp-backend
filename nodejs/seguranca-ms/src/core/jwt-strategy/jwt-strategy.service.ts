import { Injectable } from '@nestjs/common';
import { PassportStrategy } from '@nestjs/passport';
import { ExtractJwt, Strategy } from 'passport-jwt';

@Injectable()
export class JwtStrategyService extends PassportStrategy(Strategy, 'jwt') {
  constructor() {
    super({
      jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
      ignoreExpiration: false,
      secretOrKey: process.env.SECRET_PHRASE,
    });
    console.log('> DEBUG > JwtStrategyService > process.env.SECRET_PHRASE -> ', process.env.SECRET_PHRASE);
  }

  async validate(payload) {
    return payload;
  }
  
}
