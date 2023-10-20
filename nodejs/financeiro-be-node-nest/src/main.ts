import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { ValidationPipe } from '@nestjs/common';
import { DocumentBuilder, SwaggerModule } from '@nestjs/swagger';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  // swagger
  const configSwagger = new DocumentBuilder()
      .setTitle('Documentação com Swagger - Financeiro Node/NestJS')
      .setDescription('Documentação das API´s do módulo Financeiro MS')
      .setVersion('1.0')
      .addTag('financeiro')
      .build();

  const document = SwaggerModule.createDocument(app, configSwagger);
  SwaggerModule.setup('docs/api', app, document);

  // validation
  app.useGlobalPipes(new ValidationPipe());

  // start-ando servidor
  await app.listen(20110);
}

bootstrap();
