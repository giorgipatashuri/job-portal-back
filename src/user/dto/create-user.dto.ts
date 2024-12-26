import { IsEmail, IsEnum, MinLength } from 'class-validator';
import { Role } from 'src/utils/role.enum';

export class CreateUserDto {
  @MinLength(3)
  firstname: string;
  @MinLength(3)
  lastname: string;
  @IsEmail()
  email: string;
  @MinLength(6)
  password: string;
  @IsEnum(Role)
  role: Role = Role.USER;
}
