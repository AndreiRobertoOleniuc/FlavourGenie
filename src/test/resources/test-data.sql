-- Populate category table
INSERT INTO category (id, category) VALUES (1, 'Dessert');
INSERT INTO category (id, category) VALUES (2, 'Main Course');

-- Populate ingredient table
INSERT INTO ingredient (id, name, quantity, unit) VALUES (3, 'Sugar', '100', 'g');
INSERT INTO ingredient (id, name, quantity, unit) VALUES (4, 'Flour', '200', 'g');

-- Populate users table
INSERT INTO users (id,username, first_name, last_name, password, picture) VALUES
 (5,'test@test.com', 'Test', 'User', 'password', 'picture.jpg');

-- Populate recipe table
INSERT INTO recipe (id, cooking_time, description, instruction, recipe_difficulty, recipe_image, recipe_image_description, recipe_name, USER_ID) VALUES
(6, '30 minutes', 'Test description', 'Test instruction', 'Easy', 'image.jpg', 'Test image', 'Test Recipe', 5);

-- Populate recipe_categories table
INSERT INTO recipe_categories (recipe_id, categories_id) VALUES (6, 1);
INSERT INTO recipe_categories (recipe_id, categories_id) VALUES (6, 2);

-- Populate recipe_ingredients table
INSERT INTO recipe_ingredients (recipe_id, ingredients_id) VALUES (6, 3);
INSERT INTO recipe_ingredients (recipe_id, ingredients_id) VALUES (6, 4);

-- Populate feedback table
INSERT INTO feedback (id, rating, recipe_id, USER_ID) VALUES (7, 5, 6, 5);
