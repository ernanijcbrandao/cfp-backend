import {
  Body,
  Controller,
  Get,
  Param,
  Post,
  Put,
  Query,
  Res,
  Patch,
} from '@nestjs/common';
import { UserService } from './user.service';
import { RequestCreateUser } from './dto/request-create-user';
import { RequestUpdateUser } from './dto/request-update-user';
import { ApiOperation, ApiResponse, ApiTags } from '@nestjs/swagger';
import { Response } from 'express';

@ApiTags('users')
@Controller('v1/users')
export class UserController {
  constructor(private readonly userService: UserService) {}

  @Get(':id')
  @ApiOperation({ summary: 'Carregar os dados de um determinado usuário' })
  @ApiResponse({ status: 200, description: 'Dados do usuário' })
  @ApiResponse({ status: 404, description: 'ID inválido' })
  async loadById(@Param('id') id: string) {
    const user = await this.userService.load(id);
    return {
      user,
    };
  }

  @Get()
  @ApiOperation({
    summary: 'Listar dados de usuários ativos com opção de filtro por nome',
  })
  @ApiResponse({ status: 200, description: 'Lista de usuários' })
  @ApiResponse({ status: 204, description: 'Nenhum usuário encontrado' })
  async list(@Res() response: Response, @Query('name') name?: string) {
    const users = await this.userService.list(name);
    const statusCode = users.length > 0 ? 200 : 204;
    return response.status(statusCode).json({
      users,
    });
  }

  @Post()
  @ApiOperation({ summary: 'Criar um usuário' })
  @ApiResponse({ status: 201, description: 'Usuário criad0' })
  @ApiResponse({ status: 400, description: 'Parâmetro(s) inválido(s)' })
  async create(@Body() body: RequestCreateUser) {
    const user = await this.userService.create(body);
    return {
      user,
    };
  }

  @Put(':id')
  @ApiOperation({ summary: 'Atualizar dados de um determinado usuário' })
  @ApiResponse({ status: 200, description: 'Dados do usuário atualizados' })
  @ApiResponse({ status: 400, description: 'Parâmetro(s) inválido(s)' })
  @ApiResponse({ status: 404, description: 'Usuário informado é inválido' })
  @ApiResponse({ status: 406, description: 'Usuário informado está inativo' })
  @ApiResponse({ status: 409, description: 'Requisição negada' })
  async update(@Param('id') id: string, @Body() body: RequestUpdateUser) {
    const user = await this.userService.update(id, body);
    return {
      user,
    };
  }

  @Patch('/inactive:id')
  @ApiOperation({ summary: 'Inativar um determinado usuário' })
  @ApiResponse({ status: 204, description: 'Usuário inativado' })
  @ApiResponse({ status: 404, description: 'Usuário informado é inválido' })
  @ApiResponse({ status: 409, description: 'Requisição negada' })
  async inactive(@Param('id') id: string) {
    await this.userService.inactive(id);
  }
}
