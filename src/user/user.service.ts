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
  async create(createUserDto: CreateUserDto) {
    const existingUser = await this.userRepository.findOneBy({
      email: createUserDto.email,
    });
    if (existingUser) throw new ConflictException('User already Exists');

    return await this.userRepository.save(createUserDto);
  }
  async findByMail(email: string) {
    return this.userRepository.findOneBy({ email });
  }
}
