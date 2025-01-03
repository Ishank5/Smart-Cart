package com.example.smart_cart.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart_cart.SmartBasketViewModel
import com.example.smart_cart.data.ViewModels.QrState
import com.example.smart_cart.data.ViewModels.QrViewModel
import com.example.smart_cart.data.repository.AuthRepository
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.delay
import android.os.VibrationEffect
import android.os.Vibrator
import android.content.Context
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.smart_cart.data.ViewModels.GetUserState
import com.example.smart_cart.data.ViewModels.ItemsViewModel
import com.example.smart_cart.data.ViewModels.UserViewModel
import com.example.smart_cart.data.model.SmartBasket

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun QrCodeScreen(
    qrViewModel: QrViewModel,
    authRepository: AuthRepository,
    onQrSuccess: () -> Unit, // Callback to handle QR_SUCCESS (e.g., navigate back)
    smartBasketViewModel: SmartBasketViewModel,
    userViewModel: UserViewModel,
    itemsViewModel: ItemsViewModel
) {
    val qrState by qrViewModel.qrState.observeAsState()
    val qrId by qrViewModel.qrId.observeAsState()
    var qrCodeBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    val context= LocalContext.current

    // Observe smartBasketId from the ViewModel
    val smartBasketId by smartBasketViewModel.smartBasketId.observeAsState()

    // Fetch QR details when the screen opens
    LaunchedEffect(Unit) {
        qrViewModel.getQrDetails(authRepository)
    }

    // Observe qrState and generate QR code
    LaunchedEffect(qrState) {
        when (qrState) {
            is QrState.Success -> {
                qrId?.let {
                    qrCodeBitmap = generateQrCode(it.toString(), 800) // QR code size
                }
                Log.d("QrCodeScreen", "QR details fetched successfully")
            }
            is QrState.Error -> {
                Log.e("QrCodeScreen", "Error fetching QR details: ${(qrState as QrState.Error).message}")
            }
            is QrState.Loading -> {
                Log.d("QrCodeScreen", "Loading QR details...")
            }
            else -> {}
        }
    }

    // Call onQrSuccess when smartBasketId is fetched
    val userState by userViewModel.getUserState.observeAsState()
    var smartBasket by remember { mutableStateOf<SmartBasket?>(null) }
    var smartBasketId1 :Int?=1
            LaunchedEffect(smartBasketId) {
                smartBasketId?.let {
                    showDialog = true
                    // Trigger vibration
                  //  userViewModel.getUserDetails(authRepository)
                    //unpack api response and fetch id
                    userViewModel.getUserDetails(authRepository)

                    userState?.let { state ->
                        when (state) {
                            is GetUserState.Success -> {
                                val profileData = state.response.data
                                profileData?.let {
                                    smartBasket = it.smartBasket
                                     smartBasketId1=smartBasket?.id

                                }
                            }
                            is GetUserState.Error -> {
                                // Handle error state
                            }
                            else -> { /* Handle other states if needed */ }
                        }
                    }





                    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    if (vibrator.hasVibrator()) {
                        // For devices running API 26 or above
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
                        } else {
                            vibrator.vibrate(1000) // For older devices
                        }
                    }
                    delay(2000) // Show dialog for 2 seconds
                    showDialog = false
                    onQrSuccess()
                }
            }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { /* Do nothing */ },
            title = { Text(text = "Success") },
            text = { Text(text = "Cart was successfully connected") },
            buttons = {}
        )
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Scan QR",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                },
                backgroundColor = androidx.compose.ui.graphics.Color(0xFF2382AA),
                contentColor = androidx.compose.ui.graphics.Color.White,
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                },
                modifier = Modifier.height(80.dp)
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (qrState is QrState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = androidx.compose.ui.graphics.Color(0xFF2382AA)
                )
            } else {
                qrCodeBitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "QR Code",
                        modifier = Modifier.size(300.dp)
                    )
                }
            }
        }
    }
}

fun generateQrCode(data: String, size: Int): Bitmap? {
    val qrCodeWriter = QRCodeWriter()
    return try {
        val bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, size, size)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        Log.d("generateQrCode", "QR Code bitmap generated successfully")
        bitmap
    } catch (e: WriterException) {
        e.printStackTrace()
        Log.e("generateQrCode", "Error generating QR Code: ${e.message}")
        null
    }
}