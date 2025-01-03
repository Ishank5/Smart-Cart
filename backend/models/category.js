const sequelize = require("../config/database");
const { DataTypes } = require("sequelize");

const Category = sequelize.define(
    "category",
    {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true,
        },
        name: {
            type: DataTypes.STRING,
            allowNull: false,
        },
        description: {
            type: DataTypes.TEXT,
        },
    },
    {
        timestamps: false,
        underscored: true,
    }
);

module.exports = Category;
