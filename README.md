Mybdjobs is an official site of Bdjobs company where anyone can open an account freely then set up the account with filled with the valid information so that people can find real jobs here and apply those jobs by using this Mybdjobs account. 
Now I was trying an Automation testing on this website using selenium webdriver.
So firstly, I visited in this website by using their URL and open in the Chrome webdriver in full-screen.
Then I created a Excel sheet where I put some username and password for login in Mybdjobs account.
Next, I read that Excel file with selenium webdriver so that when I run the project, firstly it visits Mybdjobs login page and take input those username and password data from Excel file.
But I gave a condition here, In login page, first take the first data from excel and login into the account and log out. After logout it takes the next data from excel file automatically. So basically its continue the iteration till the last data from excel.
I gave another condition here, If any username or password is incorrect then it also takes the next data from Excel file and finish the iteration.
I also used Assertion here, If any username or password is incorrect it will give assert message.
I also added a function for HTML report generate. After run the project it will create a HTML report.
