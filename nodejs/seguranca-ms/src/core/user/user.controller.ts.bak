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
  HttpStatus,
} from '@nestjs/common';
import { UserService } from './user.service';
import { CreateUserRequest } from './dto/create-user-request';
import { UpdateUserRequest } from './dto/update-user-request';
import { ApiOperation, ApiResponse, ApiTags } from '@nestjs/swagger';
import { Response } from 'express';
import { ChangePasswordRequest } from './dto/change-password-request';
import { UserPasswordChangeRequest } from './dto/user-password-change-request';

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

    const statusCode = resultList.length > 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT;

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
  async create(@Body() body: CreateUserRequest) {
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
  async update(@Param('id') id: string, @Body() body: UpdateUserRequest) {
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
  async activate(@Res() response: Response, 
      @Param('id') id: string) {
    await this.userService.activate(id);
    return response.status(HttpStatus.NO_CONTENT).json({});
  }
  
  @Patch('/inactivate/:id')
  @ApiOperation({ summary: 'Inativar um determinado usuário' })
  @ApiResponse({ status: 200, description: 'Usuário inativado' })
  @ApiResponse({ status: 404, description: 'Usuário informado é inválido' })
  @ApiResponse({ status: 409, description: 'Requisição negada' })
  async inactivate(@Res() response: Response, 
      @Param('id') id: string) {
    await this.userService.inactivate(id);
    return response.status(HttpStatus.NO_CONTENT).json({});
  }


  @Put('/:id/changePassword')
  @ApiOperation({ summary: 'Permitir a alteração de senha do usuário' })
  @ApiResponse({ status: 204, description: 'Senha do usuário alterada com sucesso' })
  @ApiResponse({ status: 404, description: 'Usuário informado é inválido' })
  @ApiResponse({ status: 409, description: 'Requisição negada' })
  async changePassword(@Res() response: Response, 
      @Param('id') userId: string,
      @Body() body: UserPasswordChangeRequest) {
    await this.userService.changePassword(userId, body);
    return response.status(HttpStatus.NO_CONTENT).json({});
  }
  

}
