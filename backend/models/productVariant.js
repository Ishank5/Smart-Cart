const sequelize = require("../config/database");
const { DataTypes } = require("sequelize");

const ProductVariant = sequelize.define(
    "productVariant",
    {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true,
        },
        color: {
            type: DataTypes.STRING,
        },
        size: {
            type: DataTypes.STRING,
        },
        weight: {
            type: DataTypes.DECIMAL(10, 2),
            allowNull: false,
        },
        price: {
            type: DataTypes.DECIMAL(10, 2),
            allowNull: false,
        },
        stock: {
            type: DataTypes.INTEGER,
            allowNull: false,
        },
    },
    {
        timestamps: true,
        underscored: true,
    }
);

module.exports = ProductVariant;
