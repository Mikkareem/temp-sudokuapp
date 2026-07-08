package com.techullurgy.sudoku.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techullurgy.sudoku.game.Board
import com.techullurgy.sudoku.presentation.ui.bgColor
import com.techullurgy.sudoku.presentation.ui.boardAccent
import com.techullurgy.sudoku.presentation.ui.boardSelectionAccent
import com.techullurgy.sudoku.utils.col
import com.techullurgy.sudoku.utils.row

@OptIn(ExperimentalGridApi::class)
@Composable
internal fun SudokuBoard(
    board: Board,
    selections: Set<Int>,
    emptyCells: Set<Int>,
    selectedCell: Int,
    onCellSelect: (Int) -> Unit,
) {
    val borderColor = boardAccent

    Grid(
        config = {
            repeat(9) {
                row(1f / 9)
                column(1f / 9)
            }
        },
        modifier =
        Modifier
            .layout { m, c ->
                val sqC =
                    if (c.maxWidth >= c.maxHeight) {
                        Constraints.fixed(c.maxHeight, c.maxHeight)
                    } else {
                        Constraints.fixed(c.maxWidth, c.maxWidth)
                    }
                val p = m.measure(sqC)
                layout(p.width, p.height) {
                    p.place(0, 0)
                }
            }.background(bgColor)
            .border(4.dp, borderColor),
    ) {
        repeat(81) { index ->
            Box(
                modifier =
                Modifier
                    .testTag("BoardCell(${index.row},${index.col})")
                    .fillMaxSize()
                    .border(1.dp, borderColor)
                    .decorateSudokuBoardCell(index, selections, selectedCell, borderColor)
                    .clickable(onClick = { onCellSelect(index) }),
                contentAlignment = Alignment.Center,
            ) {
                if(board.isNotEmpty()) {
                    val content = board[index.row][index.col]
                    if (content != 0) {
                        Text(
                            text = content.toString(),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (emptyCells.contains(index)) boardSelectionAccent else LocalContentColor.current,
                        )
                    }
                }
            }
        }
    }
}

private fun Modifier.decorateSudokuBoardCell(
    index: Int,
    selections: Set<Int>,
    selectedCell: Int,
    color: Color,
): Modifier = drawBehind {
    if (index.col != 0 && index.col % 3 == 0) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(0f, size.height),
            strokeWidth = 4.dp.toPx(),
        )
    }
    if (index.row != 0 && index.row % 3 == 0) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = 4.dp.toPx(),
        )
    }

    if (selections.contains(index)) {
        if (selectedCell == index) {
            drawRect(
                color = boardSelectionAccent.copy(alpha = 0.65f),
            )
        } else {
            drawRect(
                color = boardSelectionAccent.copy(alpha = 0.3f),
            )
        }
    } else {
        val rowBox = index.row / 3
        val colBox = index.col / 3

        if (rowBox % 2 == colBox % 2) {
            drawRect(
                color = color.copy(alpha = 0.45f),
            )
        }
    }
}
