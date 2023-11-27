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
  BadRequestException,
  NotFoundException,
} from '@nestjs/common';
import { UserService } from './user.service';
import { RequestCreateUser } from './dto/request-create-user';
import { RequestUpdateUser } from './dto/request-update-user';
import { ApiOperation, ApiResponse, ApiTags } from '@nestjs/swagger';
import { Response } from 'express';
import { RequestChangePassword } from '../password/dto/request-change-password';

@ApiTags('users')
@Controller('v1/users')
export class UserController {
  constructor(private readonly userService: UserService) {}

  @Get(':id')
  @ApiOperation({ summary: 'Carregar os dados de um determinado usuário' })
  @ApiResponse({ status: 200, description: 'Dados do usuário' })
  @ApiResponse({ status: 404, description: 'ID inválido' })
  async loadById(@Param('id') id: string) {
    const data = await this.userService.findById(id);
    if (!data) {
      throw new NotFoundException('ID inválido');
    }
    const {name, email, login, profile, created, lastUpdate, active } = data;
    return {
      id,
      name,
      email,
      login,
      profile,
      created, 
      lastUpdate, 
      active
    };
  }

  @Get()
  @ApiOperation({
    summary: 'Listar dados de usuários com opção de filtro por nome',
  })
  @ApiResponse({ status: 200, description: 'Lista de usuários' })
  @ApiResponse({ status: 204, description: 'Nenhum usuário encontrado' })
  async list(@Res() response: Response, 
      @Query('name') _name?: string,
      @Query('active') _active?: string) {

    const resultList = await this.userService.list(_name, 
      ((_active) ? 'true' === _active : undefined));

    const statusCode = resultList.length > 0 ? 200 : 204;

    return response.status(statusCode).json(resultList.map( (item) => { 
      return {
        id: item.id, 
        name: item.name, 
        email: item.email, 
        login: item.login, 
        profile: item.profile, 
        created: item.created, 
        lastUpdate: item.lastUpdate, 
        active: item.active
      };
    }));
  }

  @Post()
  @ApiOperation({ summary: 'Criar um usuário' })
  @ApiResponse({ status: 201, description: 'Usuário criado' })
  @ApiResponse({ status: 400, description: 'Parâmetro(s) inválido(s)' })
  async create(@Body() body: RequestCreateUser) {
    const {id, name, email, login, profile, created, active } =  await this.userService.create(body);
    return {
      id,
      name, 
      email, 
      login, 
      profile, 
      created
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

  @Patch('/activate/:id')
  @ApiOperation({ summary: 'Reativar um determinado usuário' })
  @ApiResponse({ status: 200, description: 'Usuário reativado' })
  @ApiResponse({ status: 404, description: 'Usuário informado é inválido' })
  @ApiResponse({ status: 409, description: 'Requisição negada' })
  async active(@Param('id') id: string) {
    await this.userService.active(id);
  }
  
  @Patch('/inactivate/:id')
  @ApiOperation({ summary: 'Inativar um determinado usuário' })
  @ApiResponse({ status: 200, description: 'Usuário inativado' })
  @ApiResponse({ status: 404, description: 'Usuário informado é inválido' })
  @ApiResponse({ status: 409, description: 'Requisição negada' })
  async inactive(@Param('id') id: string) {
    await this.userService.inactive(id);
  }


  @Put('/changePassword/:id')
  @ApiOperation({ summary: 'Permitir a alteração de senha do usuário' })
  @ApiResponse({ status: 204, description: 'Senha do usuário alterada com sucesso' })
  @ApiResponse({ status: 404, description: 'Usuário informado é inválido' })
  @ApiResponse({ status: 409, description: 'Requisição negada' })
  async changePassword(@Res() response: Response, 
      @Param('id') userId: string,
      @Body() body: RequestChangePassword) {
    await this.userService.changePassword(userId, body);
    return response.status(204);
  }
  

}
