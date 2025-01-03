const sequelize = require("../config/database");
const { DataTypes } = require("sequelize");

// Combine both product and product variant model to store the purchased product as unmodifiable

const PurchasedProduct = sequelize.define(
    "purchasedProduct",
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
        manufacturer: {
            type: DataTypes.STRING,
            allowNull: false,
        },
        image: {
            type: DataTypes.STRING,
        },
        price: {
            type: DataTypes.DECIMAL(10, 2),
            allowNull: false,
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
        quantity: {
            type: DataTypes.INTEGER,
            allowNull: false,
        },
    },
    {
        timestamps: true,
        underscored: true,
    }
);

module.exports = PurchasedProduct;
