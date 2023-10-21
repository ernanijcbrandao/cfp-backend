import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { ValidationPipe } from '@nestjs/common';
import { DocumentBuilder, SwaggerModule } from '@nestjs/swagger';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  // swagger
  const configSwagger = new DocumentBuilder()
      .setTitle('Módulo Financeiro Node/NestJS')
      .setDescription('API´s do Módulo Financeiro MS')
      .setVersion('1.0')
      .addTag('Módulo Financeiro')
      .build();

  const document = SwaggerModule.createDocument(app, configSwagger);
  SwaggerModule.setup('docs/api', app, document);

  // validation
  app.useGlobalPipes(new ValidationPipe());

  // start-ando servidor
  await app.listen(20110);
}

bootstrap();
