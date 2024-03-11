package de.blumesladen.ui.diaryentry

import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.blumesladen.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()
        }
    })

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate)
                onDismiss()
            }

            ) {
                Text(text = stringResource(id = R.string.ok_action))
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = stringResource(id = R.string.cancel_action))
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return formatter.format(Date(millis))
}

@Composable
fun calendarTodayIcon(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "calendar_month",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20f, 23.292f)
                quadToRelative(-0.708f, 0f, -1.167f, -0.459f)
                quadToRelative(-0.458f, -0.458f, -0.458f, -1.166f)
                quadToRelative(0f, -0.709f, 0.458f, -1.167f)
                quadToRelative(0.459f, -0.458f, 1.167f, -0.458f)
                reflectiveQuadToRelative(1.167f, 0.458f)
                quadToRelative(0.458f, 0.458f, 0.458f, 1.167f)
                quadToRelative(0f, 0.708f, -0.458f, 1.166f)
                quadToRelative(-0.459f, 0.459f, -1.167f, 0.459f)
                close()
                moveToRelative(-6.667f, 0f)
                quadToRelative(-0.708f, 0f, -1.166f, -0.459f)
                quadToRelative(-0.459f, -0.458f, -0.459f, -1.166f)
                quadToRelative(0f, -0.709f, 0.459f, -1.167f)
                quadToRelative(0.458f, -0.458f, 1.166f, -0.458f)
                quadToRelative(0.709f, 0f, 1.167f, 0.458f)
                quadToRelative(0.458f, 0.458f, 0.458f, 1.167f)
                quadToRelative(0f, 0.708f, -0.458f, 1.166f)
                quadToRelative(-0.458f, 0.459f, -1.167f, 0.459f)
                close()
                moveToRelative(13.334f, 0f)
                quadToRelative(-0.709f, 0f, -1.167f, -0.459f)
                quadToRelative(-0.458f, -0.458f, -0.458f, -1.166f)
                quadToRelative(0f, -0.709f, 0.458f, -1.167f)
                quadToRelative(0.458f, -0.458f, 1.167f, -0.458f)
                quadToRelative(0.708f, 0f, 1.166f, 0.458f)
                quadToRelative(0.459f, 0.458f, 0.459f, 1.167f)
                quadToRelative(0f, 0.708f, -0.459f, 1.166f)
                quadToRelative(-0.458f, 0.459f, -1.166f, 0.459f)
                close()
                moveTo(20f, 29.958f)
                quadToRelative(-0.708f, 0f, -1.167f, -0.458f)
                quadToRelative(-0.458f, -0.458f, -0.458f, -1.167f)
                quadToRelative(0f, -0.708f, 0.458f, -1.166f)
                quadToRelative(0.459f, -0.459f, 1.167f, -0.459f)
                reflectiveQuadToRelative(1.167f, 0.459f)
                quadToRelative(0.458f, 0.458f, 0.458f, 1.166f)
                quadToRelative(0f, 0.709f, -0.458f, 1.167f)
                quadToRelative(-0.459f, 0.458f, -1.167f, 0.458f)
                close()
                moveToRelative(-6.667f, 0f)
                quadToRelative(-0.708f, 0f, -1.166f, -0.458f)
                quadToRelative(-0.459f, -0.458f, -0.459f, -1.167f)
                quadToRelative(0f, -0.708f, 0.459f, -1.166f)
                quadToRelative(0.458f, -0.459f, 1.166f, -0.459f)
                quadToRelative(0.709f, 0f, 1.167f, 0.459f)
                quadToRelative(0.458f, 0.458f, 0.458f, 1.166f)
                quadToRelative(0f, 0.709f, -0.458f, 1.167f)
                quadToRelative(-0.458f, 0.458f, -1.167f, 0.458f)
                close()
                moveToRelative(13.334f, 0f)
                quadToRelative(-0.709f, 0f, -1.167f, -0.458f)
                quadToRelative(-0.458f, -0.458f, -0.458f, -1.167f)
                quadToRelative(0f, -0.708f, 0.458f, -1.166f)
                quadToRelative(0.458f, -0.459f, 1.167f, -0.459f)
                quadToRelative(0.708f, 0f, 1.166f, 0.459f)
                quadToRelative(0.459f, 0.458f, 0.459f, 1.166f)
                quadToRelative(0f, 0.709f, -0.459f, 1.167f)
                quadToRelative(-0.458f, 0.458f, -1.166f, 0.458f)
                close()
                moveTo(7.875f, 36.375f)
                quadToRelative(-1.042f, 0f, -1.833f, -0.771f)
                quadToRelative(-0.792f, -0.771f, -0.792f, -1.854f)
                verticalLineTo(8.958f)
                quadToRelative(0f, -1.041f, 0.792f, -1.833f)
                quadToRelative(0.791f, -0.792f, 1.833f, -0.792f)
                horizontalLineToRelative(2.5f)
                verticalLineTo(4.917f)
                quadToRelative(0f, -0.542f, 0.417f, -0.959f)
                quadToRelative(0.416f, -0.416f, 0.958f, -0.416f)
                quadToRelative(0.583f, 0f, 1f, 0.416f)
                quadToRelative(0.417f, 0.417f, 0.417f, 0.959f)
                verticalLineToRelative(1.416f)
                horizontalLineToRelative(13.666f)
                verticalLineTo(4.917f)
                quadToRelative(0f, -0.542f, 0.396f, -0.959f)
                quadToRelative(0.396f, -0.416f, 0.979f, -0.416f)
                quadToRelative(0.584f, 0f, 1f, 0.416f)
                quadToRelative(0.417f, 0.417f, 0.417f, 0.959f)
                verticalLineToRelative(1.416f)
                horizontalLineToRelative(2.5f)
                quadToRelative(1.042f, 0f, 1.833f, 0.792f)
                quadToRelative(0.792f, 0.792f, 0.792f, 1.833f)
                verticalLineTo(33.75f)
                quadToRelative(0f, 1.083f, -0.792f, 1.854f)
                quadToRelative(-0.791f, 0.771f, -1.833f, 0.771f)
                close()
                moveToRelative(0f, -2.625f)
                horizontalLineToRelative(24.25f)
                verticalLineTo(16.458f)
                horizontalLineTo(7.875f)
                verticalLineTo(33.75f)
                close()
                moveToRelative(0f, -19.958f)
                horizontalLineToRelative(24.25f)
                verticalLineTo(8.958f)
                horizontalLineTo(7.875f)
                close()
                moveToRelative(0f, 0f)
                verticalLineTo(8.958f)
                verticalLineToRelative(4.834f)
                close()
            }
        }.build()
    }
}