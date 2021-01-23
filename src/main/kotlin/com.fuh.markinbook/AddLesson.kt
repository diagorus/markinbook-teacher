package com.fuh.markinbook

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.MButtonSize
import com.ccfraser.muirwik.components.button.MButtonVariant
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.form.MFormControlMargin
import com.ccfraser.muirwik.components.form.MFormControlVariant
import com.fuh.markinbook.data.*
import com.soywiz.klock.DateTime
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.InputType
import org.w3c.dom.HTMLInputElement
import react.*
import react.router.dom.useHistory
import styled.css
import styled.styledDiv

val addLesson = functionalComponent<RProps> {
    val history = useHistory()
    useEffect(emptyList()) {
        appScope.launch {
            try {
                val result = ApiRepository.currentTeacher()
                SessionManager.teacher = result
            } catch (e: Exception) {
                history.push("/auth")
            }
        }
    }
    styledDiv {
        css {
            paddingLeft = 4.spacingUnits
            paddingRight = 4.spacingUnits
        }
        mPaper {
            css {
                display = Display.flex
                flexDirection = FlexDirection.column
                alignItems = Align.center
                paddingTop = 2.spacingUnits
                paddingBottom = 2.spacingUnits
            }

            val (groups, setGroups) = useState<List<Group>>(emptyList())
            val (group, setGroup) = useState<Group?>(null)
            val (groupError, setGroupError) = useState(false)
            val (chooseGroupDialogOpened, setChooseGroupDialogOpened) = useState(false)
            mButton(
                group?.title ?: "Виберіть групу",
                color = if (groupError) {
                    MColor.secondary
                } else {
                    MColor.primary
                },
                variant = MButtonVariant.outlined,
                size = MButtonSize.large,
                onClick = {
                    appScope.launch {
                        //todo
                        val schoolId = SessionManager.teacher?.schoolId!!
                        val newGroups = ApiRepository.groups(schoolId)
                        setGroups(newGroups)
                        setChooseGroupDialogOpened(true)
                    }
                }
            ) {
                val icon = if (group == null) {
                    "add"
                } else {
                    "edit"
                }
                attrs.endIcon = mIcon(icon, addAsChild = false)
            }

            if (chooseGroupDialogOpened) {
                chooseGroupDialog {
                    onClose = {
                        setChooseGroupDialogOpened(false)
                    }
                    this.groups = groups
                    onGroupClick = {
                        setGroup(it)
                        setGroupError(false)
                    }
                }
            }

            val (disciplines, setDisciplines) = useState<List<Discipline>>(emptyList())
            val (discipline, setDiscipline) = useState<Discipline?>(null)
            val (disciplineError, setDisciplineError) = useState(false)
            val (chooseDisciplineDialogOpened, setChooseDisciplineDialogOpened) = useState(false)

            mButton(
                discipline?.title ?: "Виберіть дисципліну",
                color = if (disciplineError) {
                    MColor.secondary
                } else {
                    MColor.primary
                },
                variant = MButtonVariant.outlined,
                size = MButtonSize.large,
                onClick = {
                    appScope.launch {
                        //todo
                        val schoolId = SessionManager.teacher?.schoolId!!
                        val newGroups = ApiRepository.disciplines(schoolId)
                        setDisciplines(newGroups)
                        setChooseDisciplineDialogOpened(true)
                    }
                }
            ) {
                css {
                    marginTop = 2.spacingUnits
                }

                val icon = if (discipline == null) {
                    "add"
                } else {
                    "edit"
                }
                attrs.endIcon = mIcon(icon, addAsChild = false)
            }

            if (chooseDisciplineDialogOpened) {
                chooseDisciplineDialog {
                    onClose = {
                        setChooseDisciplineDialogOpened(false)
                    }
                    this.disciplines = disciplines
                    onDisciplineClick = {
                        setDiscipline(it)
                        setDisciplineError(false)
                    }
                }
            }

            val (date, setDate) = useState("")
            val (dateError, setDateError) = useState<String?>(null)

            val (month, setMonth) = useState("")
            val (monthError, setMonthError) = useState<String?>(null)

            val (year, setYear) = useState("")
            val (yearError, setYearError) = useState<String?>(null)

            styledDiv {
                css {
                    marginTop = 2.spacingUnits
                }
                mTextField(
                    label = "Дата",
                    error = dateError != null,
                    helperText = dateError,
                    type = InputType.dateTime,
                    value = date,
                    margin = MFormControlMargin.none,
                    onChange = {
                        val value = (it.target as HTMLInputElement).value
                        setDate(value)
                    },
                    variant = MFormControlVariant.outlined,
                )
                mTextField(
                    label = "Місяць",
                    error = monthError != null,
                    helperText = monthError,
                    type = InputType.dateTime,
                    value = month,
                    margin = MFormControlMargin.none,
                    onChange = {
                        val value = (it.target as HTMLInputElement).value
                        setMonth(value)
                    },
                    variant = MFormControlVariant.outlined,
                ) {
                    css {
                        marginLeft = 2.spacingUnits
                    }
                }

                mTextField(
                    label = "Рік",
                    error = yearError != null,
                    helperText = yearError,
                    type = InputType.dateTime,
                    value = year,
                    margin = MFormControlMargin.none,
                    onChange = {
                        val value = (it.target as HTMLInputElement).value
                        setYear(value)
                    },
                    variant = MFormControlVariant.outlined,
                ) {
                    css {
                        marginLeft = 2.spacingUnits
                    }
                }
            }

            val (hours, setHours) = useState("")
            val (hoursError, setHoursError) = useState<String?>(null)
            val (minutes, setMinutes) = useState("")
            val (minutesError, setMinutesError) = useState<String?>(null)
            styledDiv {
                css {
                    marginTop = 2.spacingUnits
                }
                mTextField(
                    label = "Години",
                    error = hoursError != null,
                    helperText = hoursError,
                    type = InputType.dateTime,
                    value = hours,
                    margin = MFormControlMargin.none,
                    onChange = {
                        val value = (it.target as HTMLInputElement).value
                        setHours(value)
                    },
                    variant = MFormControlVariant.outlined,
                )

                mTextField(
                    label = "Хвилини",
                    error = minutesError != null,
                    helperText = minutesError,
                    type = InputType.dateTime,
                    value = minutes,
                    margin = MFormControlMargin.none,
                    onChange = {
                        val value = (it.target as HTMLInputElement).value
                        setMinutes(value)
                    },
                    variant = MFormControlVariant.outlined,
                ) {
                    css {
                        marginLeft = 2.spacingUnits
                    }
                }
            }

            val (duration, setDuration) = useState("")
            val (durationError, setDurationError) = useState<String?>(null)
            mTextField(
                label = "Тривалість",
                error = durationError != null,
                helperText = durationError,
                type = InputType.dateTime,
                value = duration,
                margin = MFormControlMargin.none,
                onChange = {
                    val value = (it.target as HTMLInputElement).value
                    setDuration(value)
                },
                variant = MFormControlVariant.outlined,
            ) {
                css {
                    marginTop = 2.spacingUnits
                }
            }

            val history = useHistory()
            mButton(
                "Створити",
                color = MColor.primary,
                variant = MButtonVariant.contained,
                size = MButtonSize.large,
                onClick = {
                    appScope.launch {
                        var valid = true

                        val groupIdString = group?.id?.toString()
                        if (groupIdString == null) {
                            valid = false
                            setGroupError(true)
                        }
                        val disciplineIdString = discipline?.id?.toString()
                        if (disciplineIdString == null) {
                            valid = false
                            setDisciplineError(true)
                        }
                        val dateInt = date.toIntOrNull()
                        when (dateInt) {
                            null -> {
                                valid = false
                                setDateError("Невірний формат дати")
                            }
                            !in 1..31 -> {
                                valid = false
                                setDateError("Дата повинна бути у проміжку від 1 до 31")
                            }
                        }
                        val monthInt = month.toIntOrNull()
                        when (monthInt) {
                            null -> {
                                valid = false
                                setMonthError("Невірний формат місяця")
                            }
                            !in 1..12 -> {
                                valid = false
                                setMonthError("Місяць повинен бути у проміжку від 1 до 12")
                            }
                        }
                        val yearInt = year.toIntOrNull()
                        if (yearInt == null) {
                            valid = false
                            setYearError("Невірний формат року")
                        }
                        val hoursInt = hours.toIntOrNull()
                        when (hoursInt) {
                            null -> {
                                valid = false
                                setHoursError("Невірний формат годин")
                            }
                            !in (0..23) -> {
                                valid = false
                                setHoursError("Години повинні бути у проміжку від 0 до 23")
                            }
                        }
                        val minutesInt = minutes.toIntOrNull()
                        when (minutesInt) {
                            null -> {
                                valid = false
                                setMinutesError("Невірний формат хвилин")
                            }
                            !in (0..59) -> {
                                valid = false
                                setMinutesError("Хвилин повинні бути у проміжку від 0 до 59")
                            }
                        }

                        val durationInt = duration.toIntOrNull()
                        if (durationInt == null) {
                            valid = false
                            setDurationError("Невірний формат тривалості")
                        }

                        if (valid) {
                            val start = DateTime(
                                yearInt!!,
                                monthInt!!,
                                dateInt!!,
                                hoursInt!!,
                                minutesInt!!
                            ).unixMillisLong.toString()
                            try {
                                val newLesson =
                                    ApiRepository.addLesson(groupIdString!!, disciplineIdString!!, start, duration)
                            } catch (e: Exception) {

                            }
                            history.push("/schedule")
                        }
                    }
                }
            ) {
                css {
                    marginTop = 2.spacingUnits
                }
            }
        }
    }
}

fun RBuilder.addLesson(): ReactElement {
    return child(addLesson)
}