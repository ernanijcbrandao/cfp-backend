import { Body, Controller, Get, Param, Post, Put, Delete, HttpCode, Query, Res, Patch } from '@nestjs/common';
import { AccountService } from './account.service';
import { RequestCreateAccount } from './dto/request-create-account';
import { RequestUpdateAccount } from './dto/request-update-account';
import { ApiOperation, ApiResponse, ApiTags } from '@nestjs/swagger';
import { Response } from 'express';


@ApiTags('account')
@Controller("v1/account")
export class AccountController {

    constructor(private readonly accountService: AccountService) { }

    @Get(':id')
    @ApiOperation({ summary: 'Carregar os dados de uma determinada conta',  })
    @ApiResponse({ status: 200, description: 'Dados da conta' })
    @ApiResponse({ status: 404, description: 'ID inválido' })
    async loadById(@Param('id') id: string) {
        const account = await this.accountService.load(id);
        return {
            account,
        };
    }

    @Get()
    @ApiOperation({ summary: 'Listar dados de contas ativas de determinado owner com opção de filtro por nome',  })
    @ApiResponse({ status: 200, description: 'Lista de contas' })
    @ApiResponse({ status: 204, description: 'Nenhuma conta encontrada' })
    async list(@Res() response: Response, @Query('owner') owner: string, @Query('name') name?: string) {
        const accounts = await this.accountService.list(owner, name);
        const statusCode = accounts.length > 0 ? 200 : 204;
        return response.status(statusCode).json({
            accounts,
        });
    }

    @Post()
    @ApiOperation({ summary: 'Criar uma conta' })
    @ApiResponse({ status: 201, description: 'Conta criada' })
    @ApiResponse({ status: 400, description: 'Parâmetro(s) inválido(s)' })
    async create(@Body() body: RequestCreateAccount) {
        const account = await this.accountService.create(body);
        return {
            account,
        };
    }

    @Put(':id')
    @ApiOperation({ summary: 'Atualizar dados de uma determinada conta',  })
    @ApiResponse({ status: 200, description: 'Dados conta atualizados' })
    @ApiResponse({ status: 400, description: 'Parâmetro(s) inválido(s)' })
    @ApiResponse({ status: 404, description: 'Conta informada é inválida' })
    @ApiResponse({ status: 406, description: 'Conta informada está inativa' })
    @ApiResponse({ status: 409, description: 'Requisição negada' })
    async update(@Param('id') id: string, @Body() body: RequestUpdateAccount) {
        const account = await this.accountService.update(id, body);
        return {
            account,
        };
    }

    @Delete(':id')
    @HttpCode(204)
    @ApiOperation({ summary: 'Excluir uma determinada conta' })
    @ApiResponse({ status: 204, description: 'Conta excluída' })
    @ApiResponse({ status: 404, description: 'Conta informada é inválida' })
    @ApiResponse({ status: 409, description: 'Requisição negada' })
    async delete(@Param('id') id: string) {
        await this.accountService.delete(id);
    }

    @Patch('/inactive:id')
    @ApiOperation({ summary: 'Inativar uma determinada conta',  })
    @ApiResponse({ status: 204, description: 'Dados inativada' })
    @ApiResponse({ status: 404, description: 'Conta informada é inválida' })
    @ApiResponse({ status: 409, description: 'Requisição negada' })
    async inactive(@Param('id') id: string) {
        const account = await this.accountService.inactive(id);
    }

}