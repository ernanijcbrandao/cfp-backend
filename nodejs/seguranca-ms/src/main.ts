import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { ValidationPipe } from '@nestjs/common';
import { DocumentBuilder, SwaggerModule } from '@nestjs/swagger';

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

  // start-ando servidor
  await app.listen(20010);
}

bootstrap();
