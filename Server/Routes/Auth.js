import express from 'express';
import {
  registerUser,
  authUser,
  sendResetCode,
  resetPassword
} from '../controllers/authController.js';

const router = express.Router();

router.post('/register', registerUser);
router.post('/login', authUser);
router.post('/send-reset-code', sendResetCode);
router.post('/reset-password', resetPassword);

export default router;
