const express = require("express");
const app = express();

const dotenv = require("dotenv");
dotenv.config();

const { sequelize } = require("./models/index");
sequelize.sync({ force: true }).then(() => {
    console.log("Database Synced");
    app.listen(process.env.PORT || 3000, () => {
        console.log(`Server is running on port ${process.env.PORT || 3000}`);
    });
});
