const sequelize = require("../config/database");
const { DataTypes } = require("sequelize");

const SmartBasket = sequelize.define(
    "smartBasket",
    {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true,
        },
        totalWeight: {
            type: DataTypes.DECIMAL(10, 2),
            allowNull: false,
        },
    },
    {
        timestamps: true,
        underscored: true,
    }
);

module.exports = SmartBasket;
