-- -----------------------------------------------------
-- Table `tmontica`.`banners`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `banners` ;



CREATE TABLE IF NOT EXISTS `banners` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `use_page` VARCHAR(255) NULL DEFAULT NULL,
  `usable` TINYINT(1) NOT NULL,
  `img` VARCHAR(255) NOT NULL,
  `link` VARCHAR(255) NOT NULL,
  `start_date` DATETIME NULL DEFAULT NULL,
  `end_date` DATETIME NULL DEFAULT NULL,
  `creator_id` VARCHAR(45) NOT NULL,
  `number` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `users` ;

CREATE TABLE IF NOT EXISTS `users` (
  `name` VARCHAR(45) NOT NULL,
  `id` VARCHAR(45) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `birth_date` DATETIME NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `role` CHAR(10) NOT NULL DEFAULT 'user',
  `created_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `point` INT(11) NOT NULL DEFAULT '0',
  `is_active` TINYINT(1) NOT NULL DEFAULT 0,
  `activate_code` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `find_id`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `find_id` ;

CREATE TABLE `find_id` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `auth_code` VARCHAR(45) NULL,
  `find_ids` VARCHAR (255) NULL,
  PRIMARY KEY (`id`));

-- -----------------------------------------------------
-- Table `tmontica`.`menus`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `menus` ;



CREATE TABLE IF NOT EXISTS `menus` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name_eng` VARCHAR(45) NOT NULL,
  `product_price` INT(11) NOT NULL,
  `category_ko` VARCHAR(45) NOT NULL,
  `category_eng` VARCHAR(45) NOT NULL,
  `monthly_menu` TINYINT(1) NOT NULL,
  `usable` TINYINT(1) NOT NULL,
  `img_url` VARCHAR(255) NOT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `sell_price` INT(11) NOT NULL,
  `discount_rate` INT(11) NOT NULL DEFAULT '0',
  `created_date` DATETIME NOT NULL,
  `updated_date` DATETIME NULL DEFAULT NULL,
  `creator_id` VARCHAR(45) NOT NULL,
  `updater_id` VARCHAR(45) NULL DEFAULT NULL,
  `stock` INT(11) NOT NULL,
  `name_ko` VARCHAR(45) NOT NULL,
  `start_date` DATETIME NULL,
  `end_date` DATETIME NULL,
  PRIMARY KEY (`id`));




-- -----------------------------------------------------
-- Table `tmontica`.`cart_menus`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cart_menus` ;



CREATE TABLE IF NOT EXISTS `cart_menus` (
  `quantity` INT(11) NOT NULL,
  `option` VARCHAR(255) NULL DEFAULT NULL,
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(45) NOT NULL,
  `price` INT(11) NOT NULL,
  `menu_id` INT(11) NOT NULL,
  `direct` TINYINT(1),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Cart_item_User`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_cart_menu_menu1`
    FOREIGN KEY (`menu_id`)
    REFERENCES `menus` (`id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE);



-- -----------------------------------------------------
-- Table `tmontica`.`options`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `options` ;



CREATE TABLE IF NOT EXISTS `options` (
  `name` VARCHAR(45) NOT NULL,
  `price` INT(11) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`));




-- -----------------------------------------------------
-- Table `tmontica`.`menu_options`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `menu_options` ;



CREATE TABLE IF NOT EXISTS `menu_options` (
  `menu_id` INT(11) NOT NULL,
  `option_id` INT(11) NOT NULL,
  PRIMARY KEY (`menu_id`, `option_id`),
  CONSTRAINT `fk_menu_has_option_menu2`
    FOREIGN KEY (`menu_id`)
    REFERENCES `menus` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_menu_has_option_option2`
    FOREIGN KEY (`option_id`)
    REFERENCES `options` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);



-- -----------------------------------------------------
-- Table `tmontica`.`orders`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `orders` ;



CREATE TABLE IF NOT EXISTS `orders` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `order_date` DATETIME NOT NULL,
  `payment` VARCHAR(45) NOT NULL,
  `total_price` INT(11) NOT NULL,
  `used_point` INT(11) NULL DEFAULT NULL,
  `real_price` INT(11) NOT NULL,
  `status` CHAR(10) NOT NULL,
  `user_id` VARCHAR(45) NOT NULL,
  `user_agent` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_order_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);




-- -----------------------------------------------------
-- Table `tmontica`.`order_details`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `order_details` ;



CREATE TABLE IF NOT EXISTS `order_details` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `order_id` INT NOT NULL,
  `option` VARCHAR(255) NULL DEFAULT NULL,
  `price` INT(11) NOT NULL,
  `quantity` INT(11) NOT NULL,
  `menu_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_order_detail_menu1`
    FOREIGN KEY (`menu_id`)
    REFERENCES `menus` (`id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `fk_order_detail_order1`
    FOREIGN KEY (`order_id`)
    REFERENCES `orders` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);



-- -----------------------------------------------------
-- Table `tmontica`.`order_status_logs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `order_status_logs` ;



CREATE TABLE IF NOT EXISTS `order_status_logs` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `status_type` VARCHAR(45) NOT NULL,
  `editor_id` VARCHAR(45) NOT NULL,
  `order_id` INT NOT NULL,
  `modified_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_order_status_log_order1`
    FOREIGN KEY (`order_id`)
    REFERENCES `orders` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);





-- -----------------------------------------------------
-- Table `tmontica`.`points`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `points` ;

CREATE TABLE IF NOT EXISTS `points` (
  `user_id` VARCHAR(45) NOT NULL,
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `type` CHAR(10) NOT NULL,
  `date` DATETIME NOT NULL,
  `amount` INT(11) NOT NULL,
  `description` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_point_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);
