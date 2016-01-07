

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE html>
<html>
    <head>
        <title>pinterest</title>
        <script src="sdk.js"></script>
        <script src="pinit.js"></script>
        <link rel="stylesheet" href="css/bootstrap.min.css" />
        <link rel="stylesheet" href="css/bootstrap-theme.min.css" />
        <style>
            body{
                background-color: papayawhip;
            }
            form.settop {
                margin-top: 4cm;

            }
            form { 
                margin: 0 auto; 
                width:500px;
            }
            td {
                padding-top: 2%;
                padding-left: auto;
            }
        </style>
        <script>
            window.pAsyncInit = function () {
                PDK.init({
                    appId: 4801623521811773378, // Change this
                    cookie: true
                });
            };

            (function (d, s, id) {
                var js, pjs = d.getElementsByTagName(s)[0];
                if (d.getElementById(id)) {
                    return;
                }
                js = d.createElement(s);
                js.id = id;
                js.src = "sdk.js";
                pjs.parentNode.insertBefore(js, pjs);
            }(document, 'script', 'pinterest-jssdk'));


            PDK.login({scope: 'read_public, write_public'}, function (session) {
                if (!session) {
                    alert('The user chose not to grant permissions or closed the pop-up');
                } else {
                    console.log('Thanks for authenticating. Getting your information...');
                    PDK.me(function (response) {
                        if (!response || response.error) {
                            alert('Oops, there was a problem getting your information');
                        } else {
                            console.log('Welcome,  ' + response.data.first_name + '!');
                        }
                    });
                }
            });
        </script>

    </head>
    <body>
        <form method="post" action="pinterest/analyze" class="settop">

            <table class="fixs">
                <tr><td class="form-group "><label>Pinterest Board Name</label></td>
                    <td class="form-group"><input type="text" name="url" class="form-control" required></td>
                </tr>
                <tr><td class="form-group"><label># Pins to Analyze</label></td>
                    <td class="form-group"><input type="number" name="nopins" class="form-control" required></td>
                </tr>
                <tr><td class="form-group"><label>Offset # of Pins</label></td>
                    <td class="form-group"><input type="number" name="offset" class="form-control" required></td>
                </tr>
                <tr><td class="form-group"><label>Include Pins Filter</label></td>
                    <td class="form-group"><input type="text" name="filterwords" class="form-control" ></td>
                </tr>
                <tr><td class="form-group">
                    <input type="submit" value="Analyze" class="btn btn-primary"></td>
                </tr>
            </table>
        </form>
    </body>
</html>
