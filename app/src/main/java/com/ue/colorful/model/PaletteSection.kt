package com.ue.colorful.model

import com.ue.adapterdelegate.Item

class PaletteSection(val colorSectionName: String, val colorSectionValue: Int, val paletteColors: List<PaletteColor>) : Item {
    var isSelected = false
}
