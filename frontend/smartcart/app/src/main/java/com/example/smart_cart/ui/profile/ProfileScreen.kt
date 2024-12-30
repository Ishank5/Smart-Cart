package com.example.smart_cart.ui.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import com.example.smart_cart.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.Divider

import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smart_cart.data.ViewModels.GetUserState
import com.example.smart_cart.data.ViewModels.UserViewModel
import com.example.smart_cart.data.model.SmartBasket


@Composable
fun MainScreen(onLogoutClick: () -> Unit,userViewModel: UserViewModel) {



    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("") },
                    backgroundColor = Color(0xFF2382AA),
                    contentColor = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )
            }
        },
        bottomBar = { BottomNavigationBar() }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Image(
                painter = painterResource(R.drawable.bg_image),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Heading(modifier = Modifier.padding(top=48.dp))
                SearchBar(modifier = Modifier.padding(top = 32.dp, start = 12.dp, end = 12.dp,bottom=12.dp))

                UpdatesSection()
                CartConnectionStatus(userViewModel)
                CategoriesSection(modifier = Modifier.padding(top=12.dp,bottom = 12.dp))
                Divider(
                    color = Color(0xdddddddd),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                GrocerySection(modifier = Modifier.padding(top=4.dp,bottom = 32.dp))
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}


@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    var textState by remember { mutableStateOf(TextFieldValue("")) }

    Box(
        modifier = modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)
            Spacer(modifier=Modifier.width(8.dp))
            BasicTextField(
                value = textState,
                onValueChange = { textState = it },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.Gray, fontSize = 16.sp),
                decorationBox = { innerTextField ->
                    if (textState.text.isEmpty()) {
                        Text("Search for fruits, vegetables, groce...", color = Color.Gray)
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Composable
fun Heading(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
//        Icon(, contentDescription = "Location", tint = Color.Gray)

        Text(text = "Smart Cart", color = Color.White, fontSize = 16.sp)

    }
}

@Composable
fun UpdatesSection() {
    val images = listOf(
        R.drawable.ic_launcher_foreground, // Add more image resources to the list
        R.drawable.ic_launcher_foreground,
        R.drawable.ic_launcher_foreground
        // Add more images here as needed
    )

    Box(
        modifier = Modifier
            .fillMaxWidth() // Ensures the box takes up the full width
            .height(250.dp) // Set a height for the box, adjust as needed
            .background(Color.Transparent) // Transparent background
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(), // Ensures the row takes up the full width
            horizontalArrangement = Arrangement.Center
        ) {
            items(images) { image ->
                Image(
                    painter = painterResource(image),
                    contentDescription = "Updates",
                    modifier = Modifier
                        .size(400.dp) // Fixed size for each image
                    // Optional padding between images
                )
            }
        }
    }
}



//            Column(
//                modifier = Modifier
//
//                    .padding(16.dp)
//            ) {
//                Text(
//                    text = "Updates",
//                    color = Color.White,
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                Text(
//                    text = "All Vegetables & Fruits",
//                    color = Color.White,
//                    fontSize = 14.sp
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                Button(onClick = { /* TODO */ }) {
//                    Text(text = "See Detail")
//                }
//            }


@Composable
fun CartConnectionStatus(userViewModel: UserViewModel) {
    // Mutable state variables to track cart connection and user details
    val userState by userViewModel.getUserState.observeAsState()
    var smartBasket by remember { mutableStateOf<SmartBasket?>(null) }
    var email by remember { mutableStateOf("") }

    userState?.let { state ->
        when (state) {
            is GetUserState.Success -> {
                val profileData = state.response.data
                profileData?.let {
                    smartBasket = it.smartBasket
                    email = it.email
                }
            }
            is GetUserState.Error -> {
                // Handle error state
            }
            else -> { /* Handle other states if needed */ }
        }
    }

    Log.d("CartConnectionStatus", "Email: $email")
    Log.d("CartConnectionStatus", "Smart Basket: $smartBasket")

    val isCartConnected = smartBasket != null

    // Box container to align content at the center
    Box(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // Button styling based on the connection state
        Surface(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .width(340.dp)
                .height(80.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0x112382AA)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isCartConnected) {
                    // Text for "connected" state
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Your Cart is Connected",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2382AA)
                        )
                        Icon(
                            imageVector = Icons.Default.CheckCircle, // Replace with QR icon if available
                            contentDescription = "QR Code",
                            tint = Color(0xFF2382AA)
                        )
                    }
                } else {
                    // Text and icon for "scan QR" state
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Scan QR to connect your cart",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2382AA)
                        )
                        Icon(
                            imageVector = Icons.Default.Warning, // Replace with QR icon if available
                            contentDescription = "QR Code",
                            tint = Color(0xFF2382AA)
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun CategoriesSection(
    modifier: Modifier = Modifier
) {
//    // Initialize the repository (assuming you have a way to get it, e.g., via a singleton or DI)
//    val repository = CategoryRepository() // Use your actual repository creation logic here
//
//    // Initialize ViewModel with ViewModelFactory
//    val viewModel: CategoryViewModel = viewModel(
//        factory = CategoryViewModelFactory(repository)
//    )
//
//    // Observe categories
//    val categories by viewModel.categories.collectAsState()
//
//    Column(
//        modifier = modifier.fillMaxWidth()
//    ) {
//        Text(
//            text = "Categories",
//            color = Color.Black,
//            fontSize = 18.sp,
//            fontWeight = FontWeight.Bold
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        LazyRow(
//            horizontalArrangement = Arrangement.spacedBy(8.dp),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            items(categories) { category ->
//                CategoryItem(
//                    name = category.name,
//                    icon = painterResource(R.drawable.ic_launcher_foreground) // Replace with actual icon logic
//                )
//            }
//        }
//    }
}


@Composable
fun CategoryItem(name: String, icon: Painter) {
    Column(
        modifier = Modifier.padding(8.dp).height(90.dp).width(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = icon,
            contentDescription = name,
            modifier = Modifier.size(65.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            color = Color.Black,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}


// Data class for grocery items
data class GroceryItemData(val name: String, val category: String, val price: String, val image: Int)

// Sample list of grocery items
val groceryItems = listOf(
    GroceryItemData(name = "Tomato", category = "Vegetable", price = "100 Rs/kg", image = R.drawable.ic_launcher_foreground),
    GroceryItemData(name = "Carrot", category = "Vegetable", price = "50 Rs/kg", image = R.drawable.ic_launcher_foreground),
    GroceryItemData(name = "Apple", category = "Fruit", price = "150 Rs/kg", image = R.drawable.ic_launcher_foreground)
    // Add more items as needed
)


@Composable
fun GrocerySection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Suggested for You",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = { /* TODO */ }) {
                Text(text = "See more", color = Color.Gray)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp), // Optional: spacing between items
            modifier = Modifier.fillMaxWidth()
        ) {
            items(groceryItems) { item ->
                GroceryItem(groceryItemData = item)
            }
        }
    }
}

@Composable
fun GroceryItem(groceryItemData: GroceryItemData) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier
            .width(250.dp)
            .padding(8.dp)
            .height(128.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image section
            Image(
                painter = painterResource(groceryItemData.image),
                contentDescription = groceryItemData.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Text and button section
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp, bottom = 8.dp)
            ) {
                Text(
                    text = groceryItemData.name,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = groceryItemData.category,
                    style = MaterialTheme.typography.caption,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = groceryItemData.price,
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // Add button
            IconButton(
                onClick = { /* Add button click logic */ },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Add",
                    tint = Color(0xFF3AA14C)
                )
            }
        }
    }
}




@Composable
fun BottomNavigationBar() {
    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Gray
    ) {
        BottomNavigationItem(
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home", tint = Color(0xFF2382AA), modifier = Modifier.size(32.dp)) },
            //label = { Text("Home") },
            selected = true,
            onClick = { /* TODO */ }
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

