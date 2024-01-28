import { ValidationPipe } from '@nestjs/common';
import { NestFactory } from '@nestjs/core';
import { DocumentBuilder, SwaggerModule } from '@nestjs/swagger';
import * as dotenv from 'dotenv';
import * as moment from 'moment-timezone';
import { AppModule } from './app.module';

dotenv.config();

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  // swagger
  const configSwagger = new DocumentBuilder()
    .setTitle('Módulo Segurança Node/NestJS')
    .setDescription('API´s do Módulo Segurança MS')
    .setVersion('1.0')
    .addTag('Módulo Segurança')
    .build();

  const document = SwaggerModule.createDocument(app, configSwagger);
  SwaggerModule.setup('docs/api', app, document);

  // validation
  app.useGlobalPipes(new ValidationPipe());

  // Configurar o fuso horário para Brasília (UTC-3)
  moment.tz.setDefault('America/Sao_Paulo');

  // start-ando servidor
  await app.listen(20010);
}

bootstrap();
