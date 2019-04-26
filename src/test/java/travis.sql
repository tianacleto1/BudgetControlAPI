# Create Testuser
CREATE USER 'dev'@'localhost' IDENTIFIED BY 'password';
GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP,REFERENCES ON *.* TO 'dev'@'localhost';

# Create DB
CREATE DATABASE IF NOT EXISTS `budgetcontrol` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `budgetcontrol`;
