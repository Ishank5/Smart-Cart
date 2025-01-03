const sequelize = require("../config/database");
const { DataTypes } = require("sequelize");

const User = sequelize.define(
    "user",
    {
        id: {
            type: DataTypes.STRING,
            primaryKey: true,
            comment: "Firebase UID",
        },
        name: {
            type: DataTypes.STRING,
            allowNull: false,
        },
        email: {
            type: DataTypes.STRING,
            allowNull: false,
            unique: true,
        },
        phone: {
            type: DataTypes.STRING,
        },
    },
    {
        timestamps: true,
        underscored: true,
    }
);

module.exports = User;
