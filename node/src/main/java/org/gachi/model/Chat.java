package org.gachi.model;

import lombok.*;
import org.gachi.enums.LanguageCodes;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat")
public class Chat {
    @Id
    private Long id;

    private LanguageCodes language;
}
