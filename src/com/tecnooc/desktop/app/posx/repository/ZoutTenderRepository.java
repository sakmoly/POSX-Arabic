package com.tecnooc.desktop.app.posx.repository;

import com.tecnooc.desktop.app.posx.model.Zout;
import com.tecnooc.desktop.app.posx.model.ZoutTender;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract interface ZoutTenderRepository
  extends JpaRepository<ZoutTender, Integer>
{
  public abstract List<ZoutTender> findByZoutSid(Zout paramZout);
}
