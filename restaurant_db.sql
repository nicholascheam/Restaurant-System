-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 27, 2026 at 07:52 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `restaurant_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `item_options`
--

CREATE TABLE `item_options` (
  `id` int(11) NOT NULL,
  `menu_item_id` int(11) NOT NULL,
  `option_name` varchar(50) DEFAULT NULL,
  `option_values` varchar(255) DEFAULT NULL,
  `control_type` varchar(20) DEFAULT NULL,
  `required` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `item_options`
--

INSERT INTO `item_options` (`id`, `menu_item_id`, `option_name`, `option_values`, `control_type`, `required`) VALUES
(1, 1, 'Extras', 'Cheese,Bacon,Egg', 'radio', 0),
(2, 1, 'Cheese', 'Yes,No', 'radio', 1),
(3, 3, 'Ice', 'Normal,Less,No Ice', 'combo', 0),
(4, 3, 'Sugar', 'Normal,Less,None', 'combo', 0),
(5, 1, 'bug', 'A,B,C', 'radio', 0),
(6, 5, 'aa', '1,3,4', NULL, 0),
(7, 5, '', '1,3,4', NULL, 0),
(8, 6, 'Extras', 'Cheese,Bacon,Egg', 'checkbox', 0),
(9, 6, 'Cheese', 'Yes,No', 'radio', 0);

-- --------------------------------------------------------

--
-- Table structure for table `menu_items`
--

CREATE TABLE `menu_items` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `price` double NOT NULL,
  `description` text DEFAULT NULL,
  `stock` int(11) DEFAULT 0,
  `category` varchar(50) DEFAULT NULL,
  `active` tinyint(1) DEFAULT 1,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `menu_items`
--

INSERT INTO `menu_items` (`id`, `name`, `price`, `description`, `stock`, `category`, `active`, `created_at`) VALUES
(1, 'Burger', 8.5, 'Classic beef burger', 12, 'Burgers', 1, '2026-04-23 13:39:01'),
(2, 'Fries', 4, 'Crispy french fries', 28, 'Sides', 1, '2026-04-23 13:39:01'),
(3, 'Coke', 3, 'Cold soft drink', 21, 'Drinks', 1, '2026-04-23 13:39:01'),
(6, 'Burger', 8.5, 'Classic beef burger', 0, 'Burgers', 1, '2026-04-23 13:51:44');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `total` double DEFAULT 0,
  `status` varchar(20) DEFAULT 'Completed',
  `order_datetime` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `user_id`, `total`, `status`, `order_datetime`) VALUES
(1, 1, 8.5, 'Completed', '2026-04-23 13:39:28'),
(2, 2, 17.5, 'Completed', '2026-04-27 12:07:17'),
(3, 2, 62, 'Completed', '2026-04-27 13:48:46');

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE `order_items` (
  `id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `menu_item_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price_at_purchase` double NOT NULL,
  `subtotal` double NOT NULL,
  `custom_text` varchar(255) DEFAULT NULL,
  `added_datetime` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`id`, `order_id`, `menu_item_id`, `quantity`, `price_at_purchase`, `subtotal`, `custom_text`, `added_datetime`) VALUES
(1, 1, 1, 1, 8.5, 8.5, 'Cheese, Bacon, Egg, Yes', '2026-04-23 13:39:28'),
(2, 2, 1, 1, 8.5, 8.5, 'Cheese, Yes, A', '2026-04-27 12:07:17'),
(3, 2, 3, 3, 3, 9, '', '2026-04-27 12:07:17'),
(4, 3, 3, 1, 3, 3, '', '2026-04-27 13:48:46'),
(5, 3, 1, 5, 8.5, 42.5, 'Cheese, Yes, A', '2026-04-27 13:48:46'),
(6, 3, 2, 1, 4, 4, '', '2026-04-27 13:48:46'),
(7, 3, 2, 1, 4, 4, 'Note: Hello', '2026-04-27 13:48:46'),
(8, 3, 1, 1, 8.5, 8.5, 'Cheese, Yes, B', '2026-04-27 13:48:46');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `role` varchar(20) NOT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `role`, `created_at`) VALUES
(1, 'admin', 'admin123', 'admin', '2026-04-23 13:39:01'),
(2, 'user', '123', 'user', '2026-04-23 15:22:44');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `item_options`
--
ALTER TABLE `item_options`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `menu_items`
--
ALTER TABLE `menu_items`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `item_options`
--
ALTER TABLE `item_options`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `menu_items`
--
ALTER TABLE `menu_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
