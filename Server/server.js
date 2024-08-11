import express from 'express';
import dotenv from 'dotenv';
import connectDB from './db.js';
import authRoutes from './Routes/Auth.js';
import cors from 'cors';

dotenv.config();

connectDB();

const app = express();

// Enable CORS
app.use(cors());

app.use(express.json());

app.use('/api/v1/auth', authRoutes);

app.get('/', (req, res)=>{
    res.send(["server running fine"]);
});

const PORT = process.env.PORT || 5000;

app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
