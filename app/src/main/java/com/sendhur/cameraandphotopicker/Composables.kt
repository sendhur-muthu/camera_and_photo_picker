package com.sendhur.cameraandphotopicker

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPhotosCard(onClick: (() -> Unit)) {
    OutlinedCard(
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(width = 2.dp, Color.LightGray),
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .height(180.dp)
            .background(Color.White),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .background(Color.LightGray)
                .weight(1f)
                .fillMaxWidth()
        ) {
            Card(
                shape = RoundedCornerShape(50.dp), modifier = Modifier
                    .clip(CircleShape)
                    .align(Alignment.Center)
                    .background(Color.White)
                    .padding(2.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    tint = Color.Blue,
                    contentDescription = stringResource(id = R.string.add_photos),
                    modifier = Modifier
                        .height(32.dp)
                        .width(32.dp)
                )
            }
        }
        ContentText(
            text = (stringResource(id = R.string.add_photos)),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(2.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageItem(uri: Uri, isProfile: Boolean = false, onClick: () -> Unit) {
    Box {
        AsyncImage(
            model = uri, contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = if (isProfile) {
                Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(2.dp, Color.Red, RoundedCornerShape(10.dp))
            } else {
                Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(10.dp))
            }
        )
        Card(
            shape = RoundedCornerShape(50.dp), modifier = Modifier
                .clip(CircleShape)
                .align(Alignment.TopEnd)
                .background(Color.White)
                .clickable(onClick = onClick),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(width = 2.dp, color = Color.Gray)
        ) {
            Icon(
                imageVector = Icons.Filled.Clear,
                tint = Color.Gray,
                contentDescription = stringResource(id = R.string.delete_photo),
                modifier = Modifier
                    .height(20.dp)
                    .width(20.dp)
            )
        }
        if (isProfile) {
            ContentText(text = stringResource(R.string.profile_photo),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-8).dp)
                    .drawBehind {
                        drawRoundRect(color = Color.Red, cornerRadius = CornerRadius(50f, 50f))
                    }
                    .padding(6.dp),
                size = 14.sp,
                color = Color.White)

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogBox(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    AlertDialog(
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text(text = stringResource(R.string.yes))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = stringResource(R.string.no))
            }
        }
    )
}

@Composable
fun ImageTextRow(modifier: Modifier, text: String, painter: Painter, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painter,
                contentDescription = text,
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            ContentText(text = text)
        }
        Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = null)
    }
}

@Composable
fun HeaderText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 18.sp,
        color = Color.Black,
        modifier = modifier
    )
}

@Composable
fun ContentText(
    text: String,
    modifier: Modifier = Modifier,
    size: TextUnit = 16.sp,
    color: Color = Color.Black
) {
    Text(
        text = text, fontSize = size, modifier = modifier, color = color
    )
}