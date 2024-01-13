package com.sendhur.cameraandphotopicker.screens

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.sendhur.cameraandphotopicker.AddPhotosCard
import com.sendhur.cameraandphotopicker.AlertDialogBox
import com.sendhur.cameraandphotopicker.HeaderText
import com.sendhur.cameraandphotopicker.ImageItem
import com.sendhur.cameraandphotopicker.ImageTextRow
import com.sendhur.cameraandphotopicker.MainViewModel
import com.sendhur.cameraandphotopicker.R
import com.sendhur.cameraandphotopicker.utils.Constants
import com.sendhur.cameraandphotopicker.utils.Utils.createImageFile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Gallery(viewModel: MainViewModel) {
    val data by viewModel.list.observeAsState(initial = listOf())
    val size = Constants.totalImageSize - data.size
    val multiPhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(if (size > 1) size else 2),
        onResult = {
            viewModel.setData(it)
        }
    )
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        context,
        context.packageName + ".provider",
        file
    )
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            viewModel.setData(listOf(uri))
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
    Scaffold(topBar = { TopAppBar(title = { Text(text = stringResource(R.string.manage_photos)) }, colors =
    TopAppBarDefaults.topAppBarColors(containerColor = colorResource(id = R.color.color_red), titleContentColor = Color.White)) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var showSheet by remember { mutableStateOf(false) }
            var openCamera by remember { mutableStateOf(false) }
            var showAlert by remember { mutableStateOf(false) }
            if (data.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(20.dp)
                ) {
                    Text(text = stringResource(R.string.photos_are_the_first_thing_members_look_in_your_profile_add_multiple_photos_to_get_more_matches))
                    Spacer(modifier = Modifier.height(10.dp))
                    AddPhotosCard {
                        showSheet = true
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(
                        items = data,
                    ) { index, item ->
                        ImageItem(uri = item, isProfile = index == 0) {
                            viewModel.deletedPosition = index
                            showAlert = true
                        }
                    }
                }
            }
            Button(
                onClick = { showSheet = true }, modifier = Modifier
                    .padding(bottom = 40.dp)
                    .wrapContentSize()
            ) {
                Text(text = stringResource(R.string.add_photos))
            }
            if (showSheet) {
                ModalBottomSheet(onDismissRequest = { showSheet = false }) {
                    val modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        val context = LocalContext.current
                        HeaderText(
                            text = stringResource(id = R.string.add_photos).uppercase(),
                            modifier = modifier
                        )
                        ImageTextRow(
                            modifier = modifier,
                            text = stringResource(R.string.upload_from_gallery),
                            painter = painterResource(id = R.drawable.gallery)
                        ) {
                            if (data.size < 6) {
                                multiPhotoPicker.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            } else {
                                Toast.makeText(
                                    context,
                                    "Only 6 images can be uploaded!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                        )
                        ImageTextRow(
                            modifier = modifier,
                            text = stringResource(R.string.take_a_photo),
                            painter = painterResource(id = R.drawable.camera)
                        ) {
                            if (data.size < 6) {
                                openCamera = true
                            } else {
                                Toast.makeText(
                                    context,
                                    "Only 6 images can be uploaded!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                        )
                        ImageTextRow(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                            text = "${stringResource(R.string.whatsapp_your_photos_to_us)} ${Constants.phoneNumber}",
                            painter = painterResource(id = R.drawable.whatsapp)
                        ) {
                            openWhatsApp(context)
                        }
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
            if (showAlert) {
                AlertDialogBox(
                    onDismissRequest = { showAlert = false },
                    onConfirmation = {
                        showAlert = false
                        viewModel.deleteData()
                    },
                    dialogTitle = stringResource(id = R.string.delete_photo),
                    dialogText = stringResource(id = R.string.delete_photo_description)
                )
            }
            if (openCamera) {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
                openCamera = false
            }
        }
    }
}

fun openWhatsApp(context: Context) {
    try {
        val uri = Uri.parse("https://api.whatsapp.com/send?phone=${Constants.phoneNumber}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "WhatsApp not found!", Toast.LENGTH_SHORT).show()
    }
}


