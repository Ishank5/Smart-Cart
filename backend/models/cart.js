const sequelize = require("../config/database");
const { DataTypes } = require("sequelize");

const Cart = sequelize.define("cart", {
    quantity: {
        type: DataTypes.INTEGER,
        allowNull: false,
        defaultValue: 1,
        validate: {
            min: 1,
        },
    },
});

module.exports = Cart;
