const sequelize = require("../config/database");
const { DataTypes } = require("sequelize");

const Bill = sequelize.define(
    "bill",
    {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true,
        },
        total: {
            type: DataTypes.DECIMAL(10, 2),
            allowNull: false,
        },
        paymentMethod: {
            type: DataTypes.STRING,
            allowNull: false,
        },
        status: {
            type: DataTypes.STRING,
            allowNull: false,
            defaultValue: "unpaid",
        },
    },
    {
        timestamps: true,
        underscored: true,
    }
);

module.exports = Bill;
