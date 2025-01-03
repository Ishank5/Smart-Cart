package com.example.smart_cart.ui.cartScreen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.smart_cart.R
import com.example.smart_cart.data.ViewModels.ItemsState
import com.example.smart_cart.data.ViewModels.ItemsViewModel


@Composable
fun CartScreenUI(
    OnbackClick: () -> Unit,
    OnHomeClick: () -> Unit,
    viewModel: ItemsViewModel // Pass the ViewModel
) {
    // Observe the ViewModel's state
    val itemsState by viewModel.itemsState.observeAsState(ItemsState.Loading)

    // Convert ProductDetails to CartItemData
    val cartItems = remember(itemsState) {
        when (itemsState) {
            is ItemsState.Success -> {
                (itemsState as ItemsState.Success).items.map { productDetails ->
                    CartItemData(
                        id = productDetails.productId,
                        name = productDetails.product?.name.toString(),
                        imageUrl = productDetails.product?.image ?: "", // Use a placeholder if image is null
                        rate = productDetails.price?.toDoubleOrNull() ?: 0.0,
                        quantity = 1 // Assuming quantity is always 1 for now
                    )
                }
            }
            else -> emptyList()
        }
    }

    if (cartItems.isEmpty()) {
        EmptyCartScreen(OnHomeClick, OnbackClick)
    } else {
        CartScreen(viewModel, OnbackClick, OnHomeClick)
    }
}

@Composable
fun CartScreen(
    viewModel: ItemsViewModel, // Pass the ViewModel
    OnbackClick: () -> Unit,
    OnHomeClick: () -> Unit
) {
    // Observe the ViewModel's state
    val itemsState by viewModel.itemsState.observeAsState(ItemsState.Loading)

    // State for the list of cart items
    val cartItemsState = remember { mutableStateOf(emptyList<CartItemData>()) }

    // LaunchedEffect to update cartItemsState whenever itemsState changes
    LaunchedEffect(itemsState) {
        cartItemsState.value = when (itemsState) {
            is ItemsState.Success -> {
                (itemsState as ItemsState.Success).items.map { productDetails ->
                    CartItemData(
                        id = productDetails.productId,
                        name = productDetails.product.name.toString(),
                        imageUrl = productDetails.product?.image ?: "", // Use a placeholder if image is null
                        rate = productDetails.price?.toDoubleOrNull() ?: 0.0,
                        quantity = 1 // Assuming quantity is always 1 for now
                    )
                }
            }
            else -> emptyList()
        }
    }

    // Subtotal, tax, and total calculations
    val subtotal = remember(cartItemsState.value) {
        cartItemsState.value.sumOf { it.rate * it.quantity }
    }

    val tax = remember(subtotal) {
        subtotal * 0.18
    }

    val total = remember(subtotal, tax) {
        subtotal + tax
    }

    var itemToDelete by remember { mutableStateOf<CartItemData?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Delete item confirmation dialog
    if (showDeleteDialog && itemToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete ${itemToDelete!!.name}?") },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(Color(0xFF2382AA)),
                    onClick = {
                        itemToDelete?.let {
                            if (it.quantity > 1) {
                                cartItemsState.value = cartItemsState.value.map { item ->
                                    if (item == itemToDelete) item.copy(quantity = item.quantity - 1) else item
                                }
                            } else {
                                cartItemsState.value = cartItemsState.value.filterNot { it == itemToDelete }
                            }
                        }
                        showDeleteDialog = false
                    }
                ) {
                    Text("Yes", color = Color.White)
                }
            },
            dismissButton = {
                Button(colors = ButtonDefaults.buttonColors(Color(0xFF2382AA)), onClick = { showDeleteDialog = false }) {
                    Text("No", color = Color.White)
                }
            }
        )
    }

    // Filter items with quantity > 0
    val visibleCartItems = cartItemsState.value.filter { it.quantity > 0 }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(top = 32.dp),
                title = { Text("Items") },
                navigationIcon = {
                    IconButton(onClick = { OnbackClick() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = Color.White,
                contentColor = Color.Black,
                elevation = 0.dp
            )
        },
        bottomBar = {
            BottomNavigation(
                backgroundColor = Color.White,
                contentColor = Color.Gray
            ) {
                BottomNavigationItem(
                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home", tint = Color(0xFF2382AA), modifier = Modifier.size(32.dp)) },
                    selected = true,
                    onClick = { OnHomeClick() }
                )
                BottomNavigationItem(
                    icon = { Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favorites", tint = Color(0xFF2382AA), modifier = Modifier.size(32.dp)) },
                    selected = false,
                    onClick = { /* TODO */ }
                )
                BottomNavigationItem(
                    icon = { Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Cart", tint = Color(0xFF2382AA), modifier = Modifier.size(32.dp)) },
                    selected = false,
                    onClick = { /* TODO */ }
                )
                BottomNavigationItem(
                    icon = { Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Account", tint = Color(0xFF2382AA), modifier = Modifier.size(32.dp)) },
                    selected = false,
                    onClick = { /* TODO */ }
                )
            }
        }
    ) { paddingValues ->
        when (itemsState) {
            is ItemsState.Loading -> {
                // Show loading indicator
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ItemsState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                ) {
                    // Items
                    items(visibleCartItems) { item ->
                        CartItem(item, onDelete = {
                            itemToDelete = it
                            showDeleteDialog = true
                        })
                        Divider(color = Color.LightGray, thickness = 1.dp)
                    }

                    // Order Summary and Confirm Order Image
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        OrderSummary(subtotal = subtotal, tax = tax, total = total)
                        Spacer(modifier = Modifier.height(8.dp))
                        ConfirmOrderImage()
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
            is ItemsState.Error -> {
                // Show error message
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error: ${(itemsState as ItemsState.Error).message}")
                }
            }
        }
    }
}

@Composable
fun CartItem(item: CartItemData, onDelete: (CartItemData) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Use Coil to load images from URLs
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.name,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, style = MaterialTheme.typography.subtitle1)
            Text("Qty: ${item.quantity}", style = MaterialTheme.typography.subtitle2, color = Color.Gray)
            Text("Rs ${item.rate * item.quantity}", style = MaterialTheme.typography.subtitle2, color = Color.Gray)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onDelete(item) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

// Update CartItemData to use imageUrl instead of imageRes
data class CartItemData(
    val id: Int?,
    val name: String,
    val imageUrl: String,
    val rate: Double,
    val quantity: Int
)

@Composable
fun OrderSummary(subtotal: Double, tax: Double, total: Double) {
    Card(
        shape = RoundedCornerShape(8.dp), // Adjust the corner radius as needed
        elevation = 4.dp, // Optional: adds a slight shadow for depth
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0x22A9D5F0))
                .padding(16.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp), // Adding bottom padding
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Order Summary",
                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold) // Making the text bold
                )
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Subtotal", style = MaterialTheme.typography.subtitle2)
                Text("Rs $subtotal", style = MaterialTheme.typography.subtitle2)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Tax", style = MaterialTheme.typography.subtitle2)
                Text("Rs $tax", style = MaterialTheme.typography.subtitle2)
            }
            Divider(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total", style = MaterialTheme.typography.h6)
                Text("Rs $total", style = MaterialTheme.typography.h6)
            }
        }
    }
}

@Composable
fun ConfirmOrderImage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp) // Set the desired height
            .width(300.dp),//
        contentAlignment = Alignment.Center // Center the image horizontally
    ) {
        Image(
            painter = painterResource(R.drawable.confirm),
            contentDescription = "Confirm Order",
            modifier = Modifier
                .clickable { /* Handle image click */ }
                .size(350.dp)
            , contentScale = ContentScale.Fit
        )
    }
}

// Sample Data



@Composable
fun EmptyCartScreen(OnHomeClick: () -> Unit,OnbackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top App Bar
        TopAppBar(modifier=Modifier.padding(top=32.dp),
            title = { Text("Items", style = MaterialTheme.typography.h6) },
            navigationIcon = {
                IconButton(onClick = { OnbackClick/* Handle back navigation */ }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            backgroundColor = Color.Transparent,
            contentColor = Color(0xFF2382AA),
            elevation = 0.dp
        )

        // Center content
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .background(Color.Transparent)
            ) {
                Image(
                    painter = painterResource(R.drawable.emptyscreen), // Replace with your image resource
                    contentDescription = "Empty Cart",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Your cart is empty",
                style = MaterialTheme.typography.h6.copy(color = Color(0xFF007BA8)) // Blue text color
            )
        }

        // Bottom Navigation Bar
        BottomNavigation(
            backgroundColor = Color.White,
            contentColor = Color.Gray
        ) {
            BottomNavigationItem(
                icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home", tint = Color(0xFF2382AA), modifier = Modifier.size(32.dp)) },
                //label = { Text("Home") },
                selected = true,
                onClick = {OnHomeClick() }
            )
            BottomNavigationItem(
                icon = { Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favorites", tint = Color(0xFF2382AA), modifier = Modifier.size(32.dp)) },
                //label = { Text("Favorites") },
                selected = false,
                onClick = { /* TODO */ }
            )
            BottomNavigationItem(
                icon = { Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Cart", tint = Color(0xFF2382AA), modifier = Modifier.size(32.dp)) },
                //label = { Text("Cart") },
                selected = false,
                onClick = { /* TODO */ }
            )
            BottomNavigationItem(
                icon = { Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Account", tint = Color(0xFF2382AA), modifier = Modifier.size(32.dp)) },
                //label = { Text("Account") },
                selected = false,
                onClick = { /* TODO */ }
            )
        }
    }
}
