const sequelize = require("../config/database");
const { DataTypes } = require("sequelize");

const Transaction = sequelize.define(
    "transaction",
    {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true,
        },
        amount: {
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
            defaultValue: "pending",
        },
        // TODO: Payment gateway identifies and extras to be added
    },
    {
        timestamps: true,
        underscored: true,
    }
);

module.exports = Transaction;
