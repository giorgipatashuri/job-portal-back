import {
  Column,
  CreateDateColumn,
  Entity,
  Long,
  PrimaryGeneratedColumn,
  UpdateDateColumn,
} from 'typeorm';

@Entity('users')
export class User {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Column()
  firstname: string;

  @Column()
  username: string;

  @Column()
  password: string;

  @Column({ unique: true })
  email: string;

  @Column()
  role: string;

  @Column()
  isVerified: boolean;

  @Column({ type: 'bigint' })
  otp: number;

  @Column({ type: 'timestamp' })
  otpValidUntil: Date;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;

  // cv
}
