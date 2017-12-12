package com.ue.colorful.feature.game_phun;

import java.util.Random;

public class BetterColor {

    private static String[] colorList = {
            "#1391FF",
            "#d9881b",
            "#00AAAA",
            "#00e598",
            "#006e49",
            "#804500",
            "#ffa012",
            "#0000FF",
            "#8A2BE2",
            "#4d1284",
            "#330c57",
            "#A52A2A",
            "#cc3636",
            "#c88937",
            "#5F9EA0",
            "#467475",
            "#6edd00",
            "#D2691E",
            "#fa4300",
            "#c73500",
            "#a52c00",
            "#ff5a1d",
            "#6495ED",
            "#fecb00",
            "#DC143C",
            "#00008B",
            "#008B8B",
            "#B8860B",
            "#f1b216",
            "#A9A9A9",
            "#006400",
            "#00b900",
            "#BDB76B",
            "#8B008B",
            "#cf00cf",
            "#360036",
            "#556B2F",
            "#FF8C00",
            "#9932CC",
            "#8B0000",
            "#ff0303",
            "#d95224",
            "#8FBC8F",
            "#5c9a5c",
            "#483D8B",
            "#6457b5",
            "#2F4F4F",
            "#00CED1",
            "#9400D3",
            "#f10082",
            "#00BFFF",
            "#696969",
            "#1E90FF",
            "#B22222",
            "#ffab02",
            "#2fc22f",
            "#228B22",
            "#bb00bb",
            "#FF00FF",
            "#00004e",
            "#FFD700",
            "#DAA520",
            "#79c800",
            "#00ce00",
            "#00ce00",
            "#CD5C5C",
            "#bc3a3a",
            "#4B0082",
            "#4a4adb",
            "#20209f",
            "#74eb00",
            "#ADD8E6",
            "#6cb9d2",
            "#F08080",
            "#00f1f1",
            "#e6e61b",
            "#64e764",
            "#ff3f5c",
            "#ff5714",
            "#be3600",
            "#471400",
            "#20B2AA",
            "#87CEFA",
            "#778899",
            "#7497c5",
            "#4874ad",
            "#00bb00",
            "#00CC00",
            "#32CD32",
            "#ca7928",
            "#FF00FF",
            "#440044",
            "#800000",
            "#66CDAA",
            "#0000CD",
            "#78248d",
            "#BA55D3",
            "#6334bf",
            "#9370D8",
            "#3CB371",
            "#2c15b9",
            "#7B68EE",
            "#00e990",
            "#48D1CC",
            "#C71585",
            "#191970",
            "#f21800",
            "#ffb93e",
            "#4f3200",
            "#000080",
            "#808000",
            "#6B8E23",
            "#94c430",
            "#DA70D6",
            "#b52fb0",
            "#09e009",
            "#AFEEEE",
            "#5bdcdc",
            "#c93b6b",
            "#D87093",
            "#ffb53c",
            "#ffae2b",
            "#FFDAB9",
            "#CD853F",
            "#ba7430",
            "#c00022",
            "#DDA0DD",
            "#b541b5",
            "#B0E0E6",
            "#3db2c0",
            "#800080",
            "#663399",
            "#FF0000",
            "#9a5c5c",
            "#4169E1",
            "#8B4513",
            "#f73620",
            "#ef7711",
            "#2E8B57",
            "#3fbe77",
            "#ff7311",
            "#A0522D",
            "#24a3d7",
            "#6A5ACD",
            "#708090",
            "#830000",
            "#00cc66",
            "#D2B48C",
            "#008080",
            "#9e609e",
            "#f12500",
            "#21ccbb",
            "#db1edb",
            "#e19d1e",
            "#9ACD32"
    };

    public static String getColor() {
        Random random = new Random();
        return colorList[random.nextInt(colorList.length)];
    }
}
