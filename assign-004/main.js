//Question 1.a)
function checkform(form) {
    // get all the inputs within the submitted form
    var inputs = form.getElementsByTagName('input');
    for (var i = 0; i < inputs.length; i++) {
        // only validate the inputs that have the required attribute
        if(inputs[i].hasAttribute("required")){
            if(inputs[i].value == ""){
                // found an empty field that is required
                alert("Please fill all required fields");
                return false;
            }
        }
    }
    return true;
}

//Question 1.b)
function verifFieldsFill(id){
    //getting the actualElement thanks to its id
    var actualElement = document.getElementById(id);
    //for all the element before the actual element, if they are empty and mandatory then we set to them the disabled attribute and warn the user
    for(var i = 1; i < actualElement.id; i++){
        if(document.getElementById(i).value=="" && document.getElementById(i).hasAttribute("required")){
            alert("You must fill the mandatory fields before");
            actualElement.setAttribute("disabled", "true");
        }
    }
}

function enableFields(){
    //we enable all the fields to make sure we can finish the form at some points
    // works because keydown event is triggered before input
    var inputs=document.getElementsByTagName('input');
    for(i=0;i<inputs.length;i++){
        inputs[i].disabled=false;
    }  
}

//Question 2)
function validAge(){
    //we get the value that has been filled on change
    var age = document.getElementById("3").value;
    //if its not between 18 and 100 the user can't register
    if(17<age && age<101){
        return true;
    }else{
        alert("You must be between 18 and 100 to register");
        return false;
    }
}

//Question 3)
function verifMail(){
    // a regex but for the mail
   var mailRegex = /^[a-zA-Z0-9._-]+@[a-z0-9._-]{2,}\.[a-z]{2,4}$/;
   var email = document.getElementById("6").value;
    //we get the value of the mail and test it with the regex, if it's not ok we alert the user
   if(!mailRegex.test(email)){
       alert("Your email must be valid to register !")
      return false;
   }else{
      return true;
   }

}

//Question 4)
function passwordValid(){
    //making a regex to respect the rules of the passwords
    var strongRegex = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{6,})");
    //we get the value of the password (we print it in the console to make sure to put the same afterwards)
    var pwd = document.getElementById("7").value;
    console.log(pwd);
    //if the password doesn't respect the regex then we alert the user and he has to fill it again
    if(!strongRegex.test(pwd)){
            alert("The password must contain at least 6 characters including an uppercase letter, a digit, and a special character.")
            return false;
        }else{
            return true;
        }

}

//Question 5)
function samePassword(){
    //we get the values of the password and the confirmation of the password
    var pwd = document.getElementById("7").value;
    var confirmPwd = document.getElementById("8").value;
    //if they are not the same then we alert the user 
    if(pwd != confirmPwd){
        alert("The passwords must be the sames");
        return false;
    }else{
        return true;
    }

}

//Question 6)
function successFullyRegistered(form){
    //We check the form when clicking the button
    if(checkform(form)){
        //if the form is ok we get the date and make it more readable and we alert the user he has been registered on the date
        var fullDate = new Date();
        var date = fullDate.getDate()+"/"+fullDate.getMonth()+"/"+fullDate.getFullYear();
        alert("You have been registered on "+date.toString());
        return true;
    }
}