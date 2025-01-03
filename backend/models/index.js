const User = require("./user");
const SmartBasket = require("./smartBasket");
const Category = require("./category");
const Product = require("./product");
const ProductVariant = require("./productVariant");
const Cart = require("./cart");
const PurchasedProduct = require("./purchasedProduct");
const Bill = require("./bill");
const Transaction = require("./transaction");
const sequelize = require("../config/database");

Category.hasMany(Product);
Product.belongsTo(Category);

User.hasOne(SmartBasket);
SmartBasket.belongsTo(User);

Product.hasMany(ProductVariant, { as: "variants" });
ProductVariant.belongsTo(Product, {
    foreignKey: {
        name: "productId",
        allowNull: false,
    },
});

User.belongsToMany(ProductVariant, {
    through: Cart,
    as: "cartProductVariants",
});
ProductVariant.belongsToMany(User, { through: Cart, as: "cartUsers" });

User.belongsToMany(ProductVariant, {
    through: "wishlist",
    as: "wishlistProductVariants",
});
ProductVariant.belongsToMany(User, {
    through: "wishlist",
    as: "wishlistUsers",
});

PurchasedProduct.hasOne(ProductVariant); // Caches the product & product variant details at the time of purchase, while still maintaing a link to the original product variant

Bill.belongsToMany(PurchasedProduct, {
    through: "bill_purchased_products",
});
PurchasedProduct.belongsToMany(Bill, {
    through: "bill_purchased_products",
});

User.hasMany(Bill);
Bill.belongsTo(User, {
    foreignKey: {
        name: "userId",
        allowNull: false,
    },
});

Bill.hasMany(Transaction); // Could have failed transactions too
Transaction.belongsTo(Bill);

module.exports = {
    User,
    SmartBasket,
    Category,
    Product,
    ProductVariant,
    Cart,
    Bill,
    Transaction,
    sequelize,
};
