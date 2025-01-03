const { Sequelize } = require("sequelize");
require("dotenv").config();

// Connect to mysql database
const sequelize = new Sequelize({
    host: process.env.DB_HOST,
    port: process.env.DB_PORT,
    database: process.env.DB_NAME,
    username: process.env.DB_USER,
    password: process.env.DB_PASS,
    dialect: "mysql",
});

module.exports = sequelize;
