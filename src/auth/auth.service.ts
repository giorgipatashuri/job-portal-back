import { ConflictException, Inject, Injectable } from '@nestjs/common';
import { IAuthService } from './auth.interface';
import { CreateUserDto } from 'src/user/dto/create-user.dto';
import { authResponseDto } from './dto/auth-response.dto';
import { Services } from 'src/utils/constants';
import { UserService } from 'src/user/user.service';
import * as bcrypt from 'bcrypt';
@Injectable()
export class AuthService implements IAuthService {
  constructor(
    @Inject(Services.USER) private readonly userService: UserService,
  ) {}
  async register(dto: CreateUserDto) {
    const user = await this.userService.findByMail(dto.email);

    if (user)
      throw new ConflictException('user with this email already exists');

    const hashedPassword = await bcrypt.hash(dto.password, 10);

    return await this.userService.create({
      ...dto,
      password: hashedPassword,
    });
  }
  async login(dto: CreateUserDto): Promise<authResponseDto> {
    throw new Error('Method not implemented.');
  }
}
