package org.academy.OnlineStoreDemo.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.academy.OnlineStoreDemo.model.entity.Order;
import org.academy.OnlineStoreDemo.model.entity.Role;
import org.academy.OnlineStoreDemo.model.entity.State;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {

    private Integer id;

    @NotEmpty(message = "{nameCanNotBeEmpty}")
    @Size(max = 100, message = "{theNameCanNotContainMoreThan100Characters}")
    private String firstName;

    @NotEmpty(message = "{lastNameCanNotBeEmpty}")
    @Size(max=100, message = "{theLastNameCanNotContainMoreThan100Characters}")
    private String lastName;

    @NotEmpty(message = "{theLoginCanNotBeEmpty}")
    @Size(max=100, message = "{theLoginCanNotContainMoreThan100Characters}")
    private String login;

    @NotEmpty(message = "{thePasswordCanNotBeEmpty}")
    @Size(max=100, message = "{thePasswordCanNotContainMoreThan100Characters}")
    private String password;

    @NotEmpty (message = "{theEmailCanNotBeEmpty}")
    @Email(message = "{emailMustContainAndSymbols}")
    @Size(max=100, message = "{theEmailCanNotContainMoreThan100Characters}")
    private String email;

    @Pattern(regexp = "[^a-zA-Z]{10,15}",
            message = "{theCumberCanNotContainLettersAndMustContainBetween10And15Digits}")
    private String phoneNumber;

    private String namePhoto;

    private Role role;

    private State state;

    private List<Order> orders;

    public String getPhotosImagePath() {
        if (namePhoto == null || id == null) return null;

        return "/user-photos/" + id + "/" + namePhoto;
    }

}
