import {
  ConflictException,
  HttpCode,
  HttpException,
  Inject,
  Injectable,
} from '@nestjs/common';
import { CreateUserDto } from './dto/create-user.dto';
import { User } from './entities/user.entity';
import { Repository } from 'typeorm';
import { InjectRepository } from '@nestjs/typeorm';

@Injectable()
export class UserService {
  constructor(
    @InjectRepository(User) private readonly userRepository: Repository<User>,
  ) {}
  create(createUserDto: CreateUserDto) {
    const existingUser = this.userRepository.findBy({
      email: createUserDto.email,
    });
    if (existingUser) throw new ConflictException('User already Exists');

    return this.userRepository.save(createUserDto);
  }
}
