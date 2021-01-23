package com.fuh.markinbook.components.auth

import com.ccfraser.muirwik.components.MColor
import com.ccfraser.muirwik.components.button.MButtonSize
import com.ccfraser.muirwik.components.button.MButtonVariant
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.form.MFormControlMargin
import com.ccfraser.muirwik.components.form.MFormControlVariant
import com.ccfraser.muirwik.components.mIcon
import com.ccfraser.muirwik.components.mTextField
import com.fuh.markinbook.data.ApiRepository
import com.fuh.markinbook.data.School
import com.fuh.markinbook.data.appScope
import kotlinx.coroutines.launch
import kotlinx.html.InputType
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.br

external interface RegisterProps : RProps {
    var email: String
    var emailError: String?
    var onEmailChanged: (email: String) -> Unit

    var password: String
    var passwordError: String?
    var onPasswordChanged: (password: String) -> Unit

    var repeatPassword: String
    var repeatPasswordError: String?
    var onRepeatPasswordChanged: (repeatPassword: String) -> Unit

    var firstName: String
    var firstNameError: String?
    var onFirstNameChanged: (name: String) -> Unit

    var lastName: String
    var lastNameError: String?
    var onLastNameChanged: (lastName: String) -> Unit

    var school: School?
    var schoolError: Boolean
    var onSchoolChosen: (school: School) -> Unit

    var onRegisterClick: () -> Unit
}

val register = functionalComponent<RegisterProps> { props ->
    br { }

    mTextField(
        label = "Електронна пошта",
        error = props.emailError != null,
        helperText = props.emailError,
        type = InputType.email,
        value = props.email,
        margin = MFormControlMargin.none,
        onChange = {
            val value = (it.target as HTMLInputElement).value
            props.onEmailChanged(value)
        },
        variant = MFormControlVariant.outlined,
    )

    br { }

    mTextField(
        label = "Пароль",
        error = props.passwordError != null,
        helperText = props.passwordError,
        type = InputType.password,
        value = props.password,
        margin = MFormControlMargin.none,
        onChange = {
            val value = (it.target as HTMLInputElement).value
            props.onPasswordChanged(value)
        },
        variant = MFormControlVariant.outlined,
    )

    br { }

    mTextField(
        label = "Повторіть пароль",
        error = props.repeatPasswordError != null,
        helperText = props.repeatPasswordError,
        type = InputType.password,
        value = props.repeatPassword,
        margin = MFormControlMargin.none,
        onChange = {
            val value = (it.target as HTMLInputElement).value
            props.onRepeatPasswordChanged(value)
        },
        variant = MFormControlVariant.outlined,
    )

    br { }

    mTextField(
        label = "Імʼя",
        error = props.firstNameError != null,
        helperText = props.firstNameError,
        type = InputType.text,
        value = props.firstName,
        margin = MFormControlMargin.none,
        onChange = {
            val value = (it.target as HTMLInputElement).value
            props.onFirstNameChanged(value)
        },
        variant = MFormControlVariant.outlined,
    )

    br { }

    mTextField(
        label = "Прізвище",
        error = props.lastNameError != null,
        helperText = props.lastNameError,
        type = InputType.text,
        value = props.lastName,
        margin = MFormControlMargin.none,
        onChange = {
            val value = (it.target as HTMLInputElement).value
            props.onLastNameChanged(value)
        },
        variant = MFormControlVariant.outlined,
    )

    br { }

    val (schools, setSchools) = useState(emptyList<School>())

    val (chooseSchoolDialogOpened, setChooseSchoolDialogOpened) = useState(false)
    val schoolButtonColor = if (props.schoolError) {
        MColor.secondary
    } else {
        MColor.primary
    }
    mButton(
        props.school?.title ?: "Виберіть школу",
        color = schoolButtonColor,
        variant = MButtonVariant.outlined,
        size = MButtonSize.large,
        onClick = {
            appScope.launch {
                val newSchools = ApiRepository.schools()
                setSchools(newSchools)
                setChooseSchoolDialogOpened(true)
            }
        }
    ) {
        val icon = if (props.school == null) {
            "add"
        } else {
            "edit"
        }
        attrs.endIcon = mIcon(icon, addAsChild = false)
    }

    br { }

    mButton(
        "Зареєструватися",
        color = MColor.primary,
        variant = MButtonVariant.contained,
        size = MButtonSize.large,
        onClick = {
            props.onRegisterClick()
        }
    )

    if (chooseSchoolDialogOpened) {
        chooseSchoolDialog {
            onClose = {
                setChooseSchoolDialogOpened(false)
            }
            this.schools = schools
            onSchoolClick = {
                props.onSchoolChosen(it)
            }
        }
    }
}

fun RBuilder.register(handle: RegisterProps.() -> Unit): ReactElement {
    return child(register) {
        attrs(handle)
    }
}