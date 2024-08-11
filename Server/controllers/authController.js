import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken';
import Customer from '../Models/Consumer.js';
import ServiceProvider from '../Models/Provider.js';
import nodemailer from 'nodemailer';
import dotenv from 'dotenv';

dotenv.config();

const generateToken = (id) => {
  return jwt.sign({ id }, process.env.JWT_SECRET, {
    expiresIn: '30d',
  });
};

export const registerUser = async (req, res) => {
  const { name, email, pass, gender, userType, serviceAvailable, timeSlot, avgChargePerHour, experienceInField } = req.body;
  let userModel;

  if (userType === 'Customer') {
    userModel = Customer;
  } else if (userType === 'ServiceProvider') {
    userModel = ServiceProvider;
  } else {
    return res.status(400).json({ message: 'Invalid user type' });
  }

  try {
    const userExists = await userModel.findOne({ email });

    if (userExists) {
      return res.status(400).json({ message: 'User already exists' });
    }

    const salt = await bcrypt.genSalt(10);
    const hashedPassword = await bcrypt.hash(pass, salt);

    const user = await userModel.create({
      name,
      email,
      pass: hashedPassword,
      gender,
      ...req.body
    });

    if (user) {
      res.status(201).json({
        _id: user._id,
        name: user.name,
        email: user.email,
        token: generateToken(user._id),
      });
    } else {
      res.status(400).json({ message: 'Invalid user data' });
    }
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};

export const authUser = async (req, res) => {
  const { email, pass, userType } = req.body;
  let userModel;

  if (userType === 'Customer') {
    userModel = Customer;
  } else if (userType === 'ServiceProvider') {
    userModel = ServiceProvider;
  } else {
    return res.status(400).json({ message: 'Invalid user type' });
  }

  try {
    const user = await userModel.findOne({ email });

    if (user && (await bcrypt.compare(pass, user.pass))) {
      res.json({
        _id: user._id,
        name: user.name,
        email: user.email,
        token: generateToken(user._id),
      });
    } else {
      res.status(401).json({ message: 'Invalid email or password' });
    }
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};

const transporter = nodemailer.createTransport({
  service: 'Gmail',
  auth: {
    user: process.env.EMAIL_USER,
    pass: process.env.EMAIL_PASS
  }
});

export const sendResetCode = async (req, res) => {
  const { email, userType } = req.body;
  let userModel;

  if (userType === 'Customer') {
    userModel = Customer;
  } else if (userType === 'ServiceProvider') {
    userModel = ServiceProvider;
  } else {
    return res.status(400).json({ message: 'Invalid user type' });
  }

  try {
    const user = await userModel.findOne({ email });

    if (!user) {
      return res.status(404).json({ message: 'User not found' });
    }

    const resetCode = Math.floor(100000 + Math.random() * 900000).toString();

    user.resetCode = resetCode;
    await user.save();

    await transporter.sendMail({
      from: process.env.EMAIL_USER,
      to: user.email,
      subject: 'Password Reset Code',
      text: `Your password reset code is ${resetCode}`
    });

    res.status(200).json({ message: 'Reset code sent' });
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};

export const resetPassword = async (req, res) => {
  const { email, resetCode, newPassword, userType } = req.body;
  let userModel;

  if (userType === 'Customer') {
    userModel = Customer;
  } else if (userType === 'ServiceProvider') {
    userModel = ServiceProvider;
  } else {
    return res.status(400).json({ message: 'Invalid user type' });
  }

  try {
    const user = await userModel.findOne({ email });

    if (!user || user.resetCode !== resetCode) {
      return res.status(400).json({ message: 'Invalid reset code' });
    }

    const salt = await bcrypt.genSalt(10);
    const hashedPassword = await bcrypt.hash(newPassword, salt);

    user.pass = hashedPassword;
    user.resetCode = undefined;
    await user.save();

    res.status(200).json({ message: 'Password reset successful' });
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};
