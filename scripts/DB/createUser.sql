CREATE USER 'liqUser'@'localhost' IDENTIFIED BY 'liqUser';
CREATE DATABASE IF NOT EXISTS `liqDB`;
GRANT ALL PRIVILEGES ON liqDB.* TO 'liqUser'@'localhost';
FLUSH PRIVILEGES;
