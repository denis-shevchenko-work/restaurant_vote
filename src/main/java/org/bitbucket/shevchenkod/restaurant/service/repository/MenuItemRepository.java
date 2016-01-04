package org.bitbucket.shevchenkod.restaurant.service.repository;

import org.bitbucket.shevchenkod.restaurant.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "menuitems", path = "api/menuitems")
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
}
