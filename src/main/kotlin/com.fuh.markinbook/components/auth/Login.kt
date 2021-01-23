package com.fuh.markinbook.components.auth

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.MButtonSize
import com.ccfraser.muirwik.components.button.MButtonVariant
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.form.MFormControlMargin
import com.ccfraser.muirwik.components.form.MFormControlVariant
import kotlinx.html.InputType
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.br

external interface LoginProps : RProps {
    var email: String
    var emailError: String?
    var onEmailChanged: (email: String) -> Unit
    var password: String
    var passwordError: String?
    var onPasswordChanged: (password: String) -> Unit
    var onLoginClick: () -> Unit
}

val login = functionalComponent<LoginProps> { props ->
    br {  }

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

    br {  }

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

    mButton(
        "Увійти",
        color = MColor.primary,
        variant = MButtonVariant.contained,
        size = MButtonSize.large,
        onClick = {
            props.onLoginClick()
        }
    )
}

fun RBuilder.login(handler: LoginProps.() -> Unit): ReactElement {
    return child(login) {
        attrs(handler)
    }
}