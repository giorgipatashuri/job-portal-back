import { CreateUserDto } from 'src/user/dto/create-user.dto';
import { authResponseDto } from './dto/auth-response.dto';
import { authRequestDto } from './dto/auth-request.dto';

export interface IAuthService {
  login(dto: authRequestDto): Promise<authResponseDto>;
  register(dto: CreateUserDto);
}
