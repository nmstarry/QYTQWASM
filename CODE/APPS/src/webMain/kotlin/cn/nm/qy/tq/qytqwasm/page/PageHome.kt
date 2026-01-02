package cn.nm.qy.tq.qytqwasm.page

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import cn.nm.qy.tq.qytqwasm.cytq.*
import cn.nm.qy.tq.qytqwasm.data.daily
import cn.nm.qy.tq.qytqwasm.data.hourly
import cn.nm.qy.tq.qytqwasm.form.FORM
import cn.nm.qy.tq.qytqwasm.line.DrawManyStepBoth
import cn.nm.qy.tq.qytqwasm.line.DrawManyStepLine
import cn.nm.qy.tq.qytqwasm.line.DrawManyStepOnes
import cn.nm.qy.tq.qytqwasm.mold.ViewMold
import cn.nm.qy.tq.qytqwasm.pull.PullDownFoldView
import cn.nm.qy.tq.qytqwasm.pull.rememberPullDownMoldData
import cn.nm.qy.tq.qytqwasm.tool.MainTool
import cn.nm.qy.tq.qytqwasm.tool.sizedunt
import cn.nm.qy.tq.qytqwasm.unit.IconView
import cn.nm.qy.tq.qytqwasm.unit.TextView
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.char
import org.jetbrains.compose.resources.painterResource
import qytqwasm.apps.generated.resources.*
import kotlin.math.roundToInt
import kotlin.time.Clock

@Composable
fun PageHome(viewmold: ViewMold, skinpage: Int?, callback: () -> Unit) {


    val appsdata by viewmold.gaindata.collectAsState()

    val datacytq by remember(appsdata) {
        derivedStateOf {
            appsdata.cytqlist.sortedBy { it.serially }
        }
    }

    val pagement = rememberPagerState { datacytq.size }

    var barsname by remember { mutableStateOf("正在加载") }

    var backskip by remember { mutableStateOf(Color(0X00000000)) }

    DrawBack(backskip = backskip)

    Column(
        modifier = Modifier.fillMaxSize().systemBarsPadding(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(15.dp, Alignment.Top)
    ) {
        Row(
            modifier = Modifier.padding(top = 10.dp).fillMaxWidth().padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextView(
                modifier = Modifier.weight(1F).padding(end = 15.dp), fonttext = buildAnnotatedString {
                    append(barsname)
                }, fontsize = 20.sp, fontskin = FORM.PURE
            )
            IconView(
                painterResource(Res.drawable.iconadds), Modifier.size(25.dp).sizedunt(onclicks = callback), tintform = FORM.PURE
            )
        }

        PageMark(pagement.currentPage, pagement.pageCount)

        val realpage by remember(datacytq) {
            derivedStateOf {
                datacytq.getOrNull(pagement.currentPage) ?: datacytq.last()
            }
        }

        val pulldownmolddata = rememberPullDownMoldData()

        LaunchedEffect(realpage) {
            CytqMode.gainmode(realpage.cytqowns.result.realtime.skycon).also {
                if (backskip != it.skin) {
                    backskip = it.skin
                }
            }
            realpage.cytqcity.name.substringAfterLast(' ').also {
                if (barsname != it) {
                    barsname = it
                }
            }
            if (Clock.System.now().toEpochMilliseconds() > (realpage.maketime + 1000 * 60 * 10)) {
                pulldownmolddata.requests(realpage, viewmold, appsdata)
            }
        }

        LaunchedEffect(skinpage) {
            if (skinpage != null) {
                pagement.scrollToPage(skinpage)
            }
        }

        HorizontalPager(pagement, Modifier.fillMaxSize()) { page ->
            val realdata = remember(datacytq, page) {
                datacytq[page]
            }
            PullDownFoldView(dragment = pulldownmolddata, callback = {
                pulldownmolddata.requests(realpage, viewmold, appsdata)
            }, pullview = {
                Column(
                    modifier = Modifier.padding(bottom = 10.dp * it.realment).height(50.dp * it.realment), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val infinite = rememberInfiniteTransition()
                    val rotating by infinite.animateFloat(
                        0F, 360F, infiniteRepeatable(tween(1000, easing = LinearEasing))
                    )

                    IconView(
                        painters = painterResource(Res.drawable.iconsuns), modifier = Modifier.size(25.dp).graphicsLayer {
                            rotationZ = if (pulldownmolddata.loaddata) rotating else it.realment * 360
                        }, tintform = FORM.PURE
                    )
                    TextView(
                        modifier = Modifier.height(25.dp),
                        fonttext = pulldownmolddata.actsment.text,
                        fontskin = FORM.PURE,
                    )
                }
            }) {

                val warndata = remember(realdata) {
                    realdata.cytqowns.result.alert.content
                }
                val realairs = remember(realdata) {
                    AqisRank.airsrank(
                        realdata.cytqowns.result.realtime.airQuality.aqi.chn, CytqAqis.IAQI
                    )
                }
                val hourdata = remember(realdata) { realdata.cytqowns.result.hourly }
                val hourmaps = remember(hourdata) { hourdata.hourly() }
                val daysdata = remember(realdata) { realdata.cytqowns.result.daily }
                val daysmaps = remember(daysdata) { daysdata.daily() }

                LazyColumn(
                    modifier = Modifier.padding(horizontal = 15.dp).clip(FORM.QJYJ), contentPadding = PaddingValues(vertical = 15.dp), verticalArrangement = Arrangement.spacedBy(15.dp, Alignment.Top), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Column(
                            modifier = Modifier.fillParentMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(
                                15.dp, Alignment.CenterVertically
                            ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Column(
                                modifier = Modifier.weight(1F),
                                verticalArrangement = Arrangement.spacedBy(
                                    15.dp, Alignment.CenterVertically
                                ),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                val realmode by remember(realdata.cytqowns.result.realtime.skycon) {
                                    derivedStateOf {
                                        CytqMode.gainmode(realdata.cytqowns.result.realtime.skycon)
                                    }
                                }

                                TextView(
                                    fonttext = realdata.cytqowns.result.realtime.temperature.roundToInt().toString(), fontskin = FORM.PURE, fontfile = FORM.FATE(), autosize = TextAutoSize.StepBased(50.sp, 150.sp), modifier = Modifier.weight(1F, false)
                                )

                                TextView(
                                    fonttext = realmode.text, fontwide = FontWeight.Bold, fontskin = FORM.PURE, fontsize = 25.sp
                                )

                                TextView(
                                    fonttext = realairs.long, fontsize = 10.sp, fontskin = FORM.PURE, fontwide = FontWeight.Bold, modifier = Modifier.background(realairs.skip, CircleShape).padding(10.dp, 5.dp)
                                )
                                TextView(
                                    fonttext = MainTool.formtime(
                                        "${realdata.cytqowns.serverTime}000".toLong()
                                    ) {
                                        hour()
                                        char(':')
                                        minute()
                                    },
                                    fontsize = 10.sp,
                                    fontwide = FontWeight.Bold,
                                    fontskin = FORM.TINT,
                                )
                            }

                            TextView(
                                fonttext = buildAnnotatedString { append(realdata.cytqowns.result.forecastKeypoint) }, fontskin = FORM.PURE
                            )
                        }
                    }

                    warndata.fastForEach { item ->
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth().background(FORM.MASK, FORM.QJYJ).padding(15.dp), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.spacedBy(
                                    15.dp, Alignment.CenterVertically
                                )
                            ) {
                                val warntype = remember(item) { CytqWarn.warntype(item.code) }

                                Row(
                                    modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    TextView(
                                        fonttext = "${warntype.type}${warntype.rank}预警",
                                        fontskin = warntype.form,
                                        fontwide = FontWeight.Bold,
                                    )

                                    TextView(
                                        fonttext = item.status,
                                        fontskin = warntype.form,
                                        fontwide = FontWeight.Bold,
                                    )
                                }

                                TextView(
                                    fonttext = item.description, fontskin = FORM.PURE, mostline = 10
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    TextView(
                                        fonttext = item.source,
                                        fontskin = FORM.PURE,
                                        fontwide = FontWeight.Bold,
                                    )

                                    TextView(
                                        fonttext = MainTool.formtime(
                                            "${item.pubtimestamp}000".toLong()
                                        ) {
                                            monthNumber()
                                            char('/')
                                            day()
                                            char(' ')
                                            hour()
                                            char(':')
                                            minute()
                                        },
                                        fontwide = FontWeight.Bold,
                                        fontskin = FORM.PURE,
                                    )
                                }
                            }
                        }
                    }

                    item {
                        val hourline = remember(hourdata) { DrawManyStepOnes(hourdata.temperature.map { it.value }) }

                        val hourment = rememberLazyListState()
                        LaunchedEffect(hourment.isScrollInProgress) {
                            if (!hourment.isScrollInProgress) {
                                hourment.animateScrollToItem(hourment.firstVisibleItemIndex)
                            }
                        }
                        LazyRow(
                            modifier = Modifier.background(FORM.MASK, FORM.QJYJ).padding(vertical = 15.dp),
                            state = hourment,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            hourmaps.fastForEachIndexed { loop, item ->
                                item {
                                    Column(
                                        modifier = Modifier.fillParentMaxWidth(0.25F),
                                        verticalArrangement = Arrangement.spacedBy(
                                            15.dp, Alignment.CenterVertically
                                        ),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        TextView(
                                            fonttext = MainTool.formdate(item.temperature.datetime) {
                                                hour()
                                                char(':')
                                                minute()
                                            }, fontskin = FORM.PURE
                                        )
                                        DrawManyStepLine(
                                            Modifier.fillParentMaxWidth(), hourline, loop
                                        )

                                        val hourmode by remember(item) {
                                            derivedStateOf {
                                                CytqMode.gainmode(item.skycon.value)
                                            }
                                        }
                                        Image(
                                            painterResource(hourmode.icon), null, modifier = Modifier.size(30.dp)
                                        )
                                        TextView(
                                            fonttext = hourmode.text, fontskin = FORM.PURE
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(
                                15.dp, Alignment.CenterVertically
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(
                                    15.dp, Alignment.CenterHorizontally
                                )
                            ) {
                                LifeView(
                                    lifename = "体　感", lifetext = "${realdata.cytqowns.result.realtime.apparentTemperature}℃", lifeicon = Res.drawable.sstqheat
                                )
                                LifeView(
                                    lifename = "湿　度", lifetext = "${realdata.cytqowns.result.realtime.humidity}%", lifeicon = Res.drawable.sstqdamp
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(
                                    15.dp, Alignment.CenterHorizontally
                                )
                            ) {
                                LifeView(
                                    lifename = "能见度", lifetext = "${realdata.cytqowns.result.realtime.visibility}km", lifeicon = Res.drawable.sstqsees
                                )

                                LifeView(
                                    lifename = "降水量", lifetext = "${realdata.cytqowns.result.realtime.precipitation.local.intensity}mm", lifeicon = Res.drawable.sstqrain
                                )
                            }
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth().background(FORM.MASK, FORM.QJYJ).padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(
                                15.dp, Alignment.CenterVertically
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TextView(
                                    fonttext = "风速风向",
                                    fontskin = FORM.TINT,
                                )
                                TextView(
                                    fonttext = MainTool.formtime(
                                        "${realdata.cytqowns.serverTime}000".toLong()
                                    ) {
                                        hour()
                                        char(':')
                                        minute()
                                    },
                                    fontskin = FORM.TINT,
                                )
                            }

                            Row(
                                modifier = Modifier.height(IntrinsicSize.Max), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val cytqwind = remember(realdata) {
                                    CytqWind.windrank(realdata.cytqowns.result.realtime.wind.speed)
                                }

                                Column(
                                    modifier = Modifier.fillMaxHeight().weight(1F),
                                    verticalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    TextView(
                                        fonttext = cytqwind.text,
                                        fontskin = FORM.PURE,
                                    )
                                    TextView(
                                        fonttext = CytqWind.windturn(realdata.cytqowns.result.realtime.wind.direction),
                                        fontsize = 25.sp,
                                        fontskin = FORM.PURE,
                                    )
                                    TextView(
                                        fonttext = "${cytqwind.rank} 级",
                                        fontskin = FORM.PURE,
                                    )
                                    TextView(
                                        fonttext = "${realdata.cytqowns.result.realtime.wind.speed} KM/H",
                                        fontskin = FORM.PURE,
                                    )
                                }

                                WindFace(turnfate = realdata.cytqowns.result.realtime.wind.direction)
                            }
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth().background(FORM.MASK, FORM.QJYJ).padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(
                                15.dp, Alignment.CenterVertically
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TextView(
                                    fonttext = "空气质量",
                                    fontskin = FORM.TINT,
                                )

                                TextView(
                                    fonttext = MainTool.formtime(
                                        "${realdata.cytqowns.result.realtime.airQuality.obsTime}000".toLong()
                                    ) {
                                        monthNumber()
                                        char('/')
                                        day()
                                        char(' ')
                                        hour()
                                        char(':')
                                        minute()
                                    },
                                    fontskin = FORM.TINT,
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    AirsArcs(
                                        progress = (realdata.cytqowns.result.realtime.airQuality.aqi.chn / 500F).coerceIn(
                                            0F, 1F
                                        )
                                    )
                                    TextView(
                                        fonttext = realdata.cytqowns.result.realtime.airQuality.aqi.chn.toString(),
                                        fontskin = FORM.PURE,
                                        fontsize = 25.sp,
                                    )
                                    TextView(
                                        fonttext = realairs.long, fontskin = FORM.PURE, modifier = Modifier.align(Alignment.BottomCenter)
                                    )
                                }
                                Column(
                                    modifier = Modifier.width(IntrinsicSize.Max).fillMaxHeight(), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    AirsItem(
                                        "PM2.5", "细微颗粒", realdata.cytqowns.result.realtime.airQuality.pm25IaqiChn
                                    )
                                    AirsItem(
                                        "PM10", "粗微颗粒", realdata.cytqowns.result.realtime.airQuality.pm10IaqiChn
                                    )
                                    AirsItem(
                                        "O₃", "臭氧浓度", realdata.cytqowns.result.realtime.airQuality.o3IaqiChn
                                    )
                                }
                                Column(
                                    modifier = Modifier.width(IntrinsicSize.Max).fillMaxHeight(), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    AirsItem(
                                        "SO₂", "二氧化硫", realdata.cytqowns.result.realtime.airQuality.so2IaqiChn
                                    )

                                    AirsItem(
                                        "NO₂", "二氧化氮", realdata.cytqowns.result.realtime.airQuality.no2IaqiChn
                                    )

                                    AirsItem(
                                        "CO", "一氧化碳", realdata.cytqowns.result.realtime.airQuality.coIaqiChn
                                    )
                                }
                            }
                        }
                    }

                    item {
                        val daysline = remember(daysmaps) {
                            DrawManyStepBoth(daysmaps.map { it.temperature.max }, daysmaps.map { it.temperature.min })
                        }
                        val daysment = rememberLazyListState()
                        LaunchedEffect(daysment.isScrollInProgress) {
                            if (!daysment.isScrollInProgress) {
                                daysment.animateScrollToItem(daysment.firstVisibleItemIndex)
                            }
                        }
                        LazyRow(
                            modifier = Modifier.background(FORM.MASK, FORM.QJYJ).padding(vertical = 15.dp), state = daysment, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                        ) {
                            daysmaps.fastForEachIndexed { loop, item ->
                                item {
                                    Column(
                                        modifier = Modifier.fillParentMaxWidth(0.25F),
                                        verticalArrangement = Arrangement.spacedBy(
                                            15.dp, Alignment.CenterVertically
                                        ),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        TextView(
                                            fonttext = MainTool.formdate(item.astro.date) {
                                                dayOfWeek(
                                                    DayOfWeekNames(
                                                        "周一", "周二", "周三", "周四", "周五", "周六", "周日"
                                                    )
                                                )
                                            }, fontskin = FORM.PURE
                                        )
                                        TextView(
                                            fonttext = MainTool.formdate(item.astro.date) {
                                                monthNumber()
                                                char('/')
                                                day()
                                            }, fontskin = FORM.PURE
                                        )

                                        val daysmode by remember(item) {
                                            derivedStateOf {
                                                CytqMode.gainmode(item.skycon08h20h.value)
                                            }
                                        }
                                        Image(
                                            painterResource(daysmode.icon), null, modifier = Modifier.size(30.dp)
                                        )
                                        TextView(
                                            fonttext = daysmode.text, fontskin = FORM.PURE
                                        )

                                        DrawManyStepLine(
                                            Modifier.fillParentMaxWidth(), daysline, loop
                                        )

                                        val darkmode by remember(item) {
                                            derivedStateOf {
                                                CytqMode.gainmode(item.skycon20h32h.value)
                                            }
                                        }
                                        Image(
                                            painterResource(darkmode.icon), null, modifier = Modifier.size(30.dp)
                                        )
                                        TextView(
                                            fonttext = darkmode.text, fontskin = FORM.PURE
                                        )
                                        val daysairs = remember(item) {
                                            AqisRank.airsrank(
                                                item.airQuality.aqi?.avg?.chn, CytqAqis.IAQI
                                            )
                                        }
                                        TextView(
                                            fonttext = daysairs.text, fontskin = FORM.PURE, fontsize = 10.sp, fontwide = FontWeight.Bold, modifier = Modifier.background(daysairs.skip, CircleShape).padding(10.dp, 5.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(
                                15.dp, Alignment.CenterVertically
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(
                                    15.dp, Alignment.CenterHorizontally
                                )
                            ) {
                                LifeView(
                                    lifename = "穿　衣", lifetext = daysdata.lifeIndex.dressing.first().desc, lifeicon = Res.drawable.shzscoat
                                )

                                LifeView(
                                    lifename = "感　冒", lifetext = daysdata.lifeIndex.coldRisk.first().desc, lifeicon = Res.drawable.shzsdrug
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(
                                    15.dp, Alignment.CenterHorizontally
                                )
                            ) {
                                LifeView(
                                    lifename = "紫外线", lifetext = daysdata.lifeIndex.ultraviolet.first().desc, lifeicon = Res.drawable.shzsrays
                                )

                                LifeView(
                                    lifename = "舒适度", lifetext = daysdata.lifeIndex.comfort.first().desc, lifeicon = Res.drawable.shzsheat
                                )
                            }
                        }
                    }

                    item {
                        Box(
                            modifier = Modifier.background(FORM.MASK, FORM.QJYJ).padding(vertical = 15.dp), contentAlignment = Alignment.Center
                        ) {

                            var progress by remember { mutableFloatStateOf(0F) }
                            val animater by animateFloatAsState(
                                progress, tween((2000 * progress).roundToInt())
                            )

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(
                                    15.dp, Alignment.CenterVertically
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 15.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    TextView(
                                        fonttext = "日出日落",
                                        fontskin = FORM.TINT,
                                    )
                                    TextView(
                                        fonttext =

                                            MainTool.formdate(daysdata.astro.first().date) {
                                                monthNumber()
                                                char('/')
                                                day()
                                                char(' ')
                                                hour()
                                                char(':')
                                                minute()
                                            },
                                        fontskin = FORM.TINT,
                                    )
                                }

                                val sunsrise by remember {
                                    mutableDoubleStateOf(
                                        MainTool.formhour(daysdata.astro.first().sunrise.time) ?: 0.0
                                    )
                                }

                                val sunsdrop by remember {
                                    mutableDoubleStateOf(
                                        MainTool.formhour(daysdata.astro.first().sunset.time) ?: 0.0
                                    )
                                }

                                LaunchedEffect(realpage) {
                                    progress = ((Clock.System.now().toEpochMilliseconds().toDouble() - sunsrise) / (sunsdrop - sunsrise)).toFloat().coerceIn(0F, 1F)
                                }
                                SunsSite(animater)
                            }

                            TextView(
                                fonttext = "${(animater * 100).roundToInt()}%", fontskin = FORM.PURE, modifier = Modifier.padding(top = 50.dp)
                            )

                            Column(
                                modifier = Modifier.align(Alignment.CenterStart).padding(start = 15.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(
                                    15.dp, Alignment.CenterVertically
                                )
                            ) {
                                IconView(
                                    painterResource(Res.drawable.sunsrise), Modifier.size(25.dp), tintform = FORM.PURE
                                )
                                TextView(
                                    fonttext = daysdata.astro.first().sunrise.time, fontskin = FORM.PURE
                                )
                            }

                            Column(
                                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 15.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(
                                    15.dp, Alignment.CenterVertically
                                )
                            ) {
                                IconView(
                                    painterResource(Res.drawable.sunsdrop), Modifier.size(25.dp), tintform = FORM.PURE

                                )
                                TextView(
                                    fonttext = daysdata.astro.first().sunset.time, fontskin = FORM.PURE
                                )
                            }
                        }
                    }

                    item {
                        TextView(
                            fonttext = "数据来自彩云天气", fontsize = 10.sp, fontwide = FontWeight.Bold, fontskin = FORM.PURE
                        )
                    }
                }
            }

        }

    }
}
