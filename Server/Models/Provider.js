import mongoose from 'mongoose';

const userSchema = new mongoose.Schema({
  name: {
    type: String,
    required: true,
    trim: true
  },
  email: {
    type: String,
    required: true,
    unique: true,
    lowercase: true,
    trim: true
  },
  pass: {
    type: String,
    required: true
  },
  gender: {
    type: String,
    enum: ['Male', 'Female', 'Other'],
    required: true
  },
  serviceAvailable: {
    type: String,
    required: true
  },
  timeSlot: {
    type: String,
    required: true
  },
  avgChargePerHour: {
    type: Number,
    required: true
  },
  experienceInField: {
    months: {
      type: Number,
      required: true
    },
    years: {
      type: Number,
      required: true
    }
  }
});

const User = mongoose.model('Provider', userSchema);

export default User;